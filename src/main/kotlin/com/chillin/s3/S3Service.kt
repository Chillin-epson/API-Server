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
import java.util.*

@Service
class S3Service(
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
    private val bucketName: String,
    private val durationMinutes: Long
) {

    private val logger = LoggerFactory.getLogger(S3Service::class.java)

    fun uploadImage(url: String, prompt: String): Pair<String, String> {
        val filename = "generated/${UUID.randomUUID()}.${MediaSubtype.JPEG.value}"
        val contentType = MediaSubtype.parse(filename).toMediaTypeValue()
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(filename)
            .contentType(contentType)
            .metadata(mapOf("x-amz-meta-prompt" to prompt))
            .build()

        val imageData = URL(url).openStream().use(InputStream::readBytes)
        s3Client.putObject(request, RequestBody.fromBytes(imageData))

        return Pair(filename, getImageUrl(filename))
    }

    fun uploadImage(file: MultipartFile): Pair<String, String> {
        val extension = MediaSubtype.parse(file.contentType).value
        val filename = "scanned/${UUID.randomUUID()}.$extension"
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(filename)
            .contentType(file.contentType)
            .build()

        s3Client.putObject(request, RequestBody.fromBytes(file.bytes))
        logger.info("Uploaded file to S3={}", filename)

        return Pair(filename, getImageUrl(filename))
    }

    fun getImageUrl(filename: String): String {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(filename)
            .build()

        val getObjectPresignRequest = GetObjectPresignRequest.builder()
            .getObjectRequest(getObjectRequest)
            .signatureDuration(Duration.ofMinutes(durationMinutes))
            .build()

        return s3Presigner.presignGetObject(getObjectPresignRequest)
            .url()
            .toString()
    }

    fun getImageData(filename: String): Pair<ByteArray, String> {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(filename)
            .build()

        return s3Client.getObjectAsBytes(getObjectRequest).let {
            val subtype = it.response().contentType()
            val contentType = MediaSubtype.parse(subtype).toMediaTypeValue()
            Pair(it.asByteArray(), contentType)
        }
    }
}