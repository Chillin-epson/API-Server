package com.chillin.s3

import org.springframework.http.MediaType
import org.springframework.stereotype.Service
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
    fun uploadImage(filename: String, url: String, prompt: String): String {
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(filename)
            .contentType(MediaType.IMAGE_JPEG_VALUE)
            .metadata(mapOf("x-amz-meta-prompt" to prompt))
            .build()

        val imageData = URL(url).openStream().use(InputStream::readBytes)
        s3Client.putObject(request, RequestBody.fromBytes(imageData))

        return getImageUrl(filename)
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
}