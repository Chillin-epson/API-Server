package com.chillin.s3

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Configuration
class S3Config {

    @Value("\${custom.aws.accessKey}")
    private lateinit var accessKey: String

    @Value("\${custom.aws.secretAccessKey}")
    private lateinit var secretAcessKey: String

    @Value("\${custom.aws.bucket.name}")
    private lateinit var bucketName: String

    @Value("\${custom.aws.durationMinutes}")
    private lateinit var durationMinutes: String

    @Bean
    fun credentialsProvider(): StaticCredentialsProvider {
        return StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretAcessKey)
        )
    }

    @Bean
    fun s3Client(credentialsProvider: StaticCredentialsProvider): S3Client {
        return S3Client.builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(credentialsProvider)
            .build()
    }

    @Bean
    fun s3Presigner(credentialsProvider: StaticCredentialsProvider): S3Presigner {
        return S3Presigner.builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(credentialsProvider)
            .build()
    }

    @Bean
    fun bucketName(): String {
        return bucketName
    }

    @Bean
    fun durationMinutes(): Long {
        return durationMinutes.toLong()
    }
}