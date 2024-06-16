package com.chillin.s3

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class S3Config {

    @Value("\${custom.aws.accessKey}")
    private lateinit var accessKey: String

    @Value("\${custom.aws.secretAccessKey}")
    private lateinit var secretAcessKey: String

    @Value("\${custom.aws.bucketName}")
    private lateinit var bucketName: String

    @Bean
    fun s3Client(): S3Client {
        val credentials = AwsBasicCredentials.create(accessKey, secretAcessKey)
        val credentialsProvider = StaticCredentialsProvider.create(credentials)

        return S3Client.builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(credentialsProvider)
            .build()
    }

    @Bean
    fun bucketName(): String {
        return bucketName
    }
}