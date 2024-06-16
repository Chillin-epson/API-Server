package com.chillin.s3

import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Service
class S3Service(
    private val s3Client: S3Client,
    private val bucketName: String,
) {
    fun uploadImage(filename: String, imageBytes: ByteArray) {
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key("$filename.jpeg")
            .contentType(MediaType.IMAGE_JPEG_VALUE)
            .build()
        s3Client.putObject(request, RequestBody.fromBytes(imageBytes))
    }
}