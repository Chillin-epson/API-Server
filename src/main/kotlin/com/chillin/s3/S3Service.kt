package com.chillin.s3

import com.chillin.type.MediaSubtype
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.io.InputStream
import java.net.URL
import java.time.Duration

@Service
class S3Service(
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
    private val bucketName: String,
    private val durationMinutes: Long
) {
    private val logger = LoggerFactory.getLogger(S3Service::class.java)

    fun uploadImage(pathname: String, url: String, prompt: String): String {
        logger.info("Uploading file from \"$url\" to S3...")
        logger.info("metadata: {\"x-amz-meta-prompt\": \"$prompt\"}")

        val contentType = MediaSubtype.parse(pathname).toMediaTypeValue()
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(pathname)
            .contentType(contentType)
            .metadata(mapOf("x-amz-meta-prompt" to prompt))
            .build()

        val imageData = URL(url).openStream().use(InputStream::readBytes)
        s3Client.putObject(request, RequestBody.fromBytes(imageData))
        logger.info("Uploaded file to S3 in \"${pathname}\"")

        return getImageUrl(pathname)
    }

    fun uploadImage(pathname: String, file: MultipartFile): String {
        logger.info("Uploading ${file.originalFilename} to S3...")

        val contentType = MediaSubtype.parse(pathname).toMediaTypeValue()
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(pathname)
            .contentType(contentType)
            .build()

        s3Client.putObject(request, RequestBody.fromBytes(file.bytes))
        logger.info("Uploaded file to S3 in \"${pathname}\"")

        return getImageUrl(pathname)
    }

    fun uploadImage(pathname: String, bytes: ByteArray): Pair<String, String> {
        logger.info("Uploading bytes to S3...")

        val contentType = MediaSubtype.parse(pathname).toMediaTypeValue()
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(pathname)
            .contentType(contentType)
            .build()

        s3Client.putObject(request, RequestBody.fromBytes(bytes))
        logger.info("Uploaded file to S3 in \"${pathname}\"")

        return Pair(pathname, getImageUrl(pathname))
    }

    fun getImageUrl(pathname: String): String {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(pathname)
            .build()

        val getObjectPresignRequest = GetObjectPresignRequest.builder()
            .getObjectRequest(getObjectRequest)
            .signatureDuration(Duration.ofMinutes(durationMinutes))
            .build()

        return s3Presigner.presignGetObject(getObjectPresignRequest)
            .url()
            .toString()
    }

    fun getImageUrlForPOST(pathname: String): String {
        logger.info("Generating presigned URL for uploading image to S3...: pathname=$pathname")

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(pathname)
            .build()

        val request = PutObjectPresignRequest.builder()
            .putObjectRequest(putObjectRequest)
            .signatureDuration(Duration.ofMinutes(durationMinutes))
            .build()

        return s3Presigner.presignPutObject(request).let {
            logger.info("Generated presigned URL for uploading image to S3: ${it.url()}")
            it.url().toExternalForm()
        }
    }

    fun getImageData(pathname: String): Pair<ByteArray, String> {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(pathname)
            .build()

        return s3Client.getObjectAsBytes(getObjectRequest).let {
            val subtype = it.response().contentType()
            val contentType = MediaSubtype.parse(subtype).toMediaTypeValue()

            logger.info("Downloaded \"$pathname\" from S3")
            logger.info("Content-Type: $contentType")

            Pair(it.asByteArray(), contentType)
        }
    }
}