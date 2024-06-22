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
        val contentType = MediaSubtype.parse(pathname).toMediaTypeValue()
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(pathname)
            .contentType(contentType)
            .metadata(mapOf("x-amz-meta-prompt" to prompt))
            .build()

        val imageData = URL(url).openStream().use(InputStream::readBytes)
        s3Client.putObject(request, RequestBody.fromBytes(imageData))
        logger.info("Uploaded file to S3={}", pathname)

        return getImageUrl(pathname)
    }

    fun uploadImage(pathname: String, file: MultipartFile): String {
        val contentType = MediaSubtype.parse(pathname).toMediaTypeValue()
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(pathname)
            .contentType(contentType)
            .build()

        s3Client.putObject(request, RequestBody.fromBytes(file.bytes))
        logger.info("Uploaded file to S3={}", pathname)

        return getImageUrl(pathname)
    }

    fun uploadImage(pathname: String, bytes: ByteArray): Pair<String, String> {
        val contentType = MediaSubtype.parse(pathname).toMediaTypeValue()
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(pathname)
            .contentType(contentType)
            .build()

        s3Client.putObject(request, RequestBody.fromBytes(bytes))
        logger.info("Uploaded file to S3={}", pathname)

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

    fun getImageData(pathname: String): Pair<ByteArray, String> {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(pathname)
            .build()

        return s3Client.getObjectAsBytes(getObjectRequest).let {
            val subtype = it.response().contentType()
            val contentType = MediaSubtype.parse(subtype).toMediaTypeValue()
            Pair(it.asByteArray(), contentType)
        }
    }
}