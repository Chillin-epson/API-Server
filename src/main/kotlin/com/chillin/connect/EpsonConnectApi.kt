package com.chillin.connect

import com.chillin.connect.request.PrintOptions
import com.chillin.connect.request.PrintSettingsRequest
import com.chillin.connect.response.AuthenticationResponse
import com.chillin.connect.response.PrintSettingsResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class EpsonConnectApi(
    private val epsonConnectProperties: EpsonConnectProperties,
    private val epsonConnectClient: EpsonConnectClient,
    private val redisTemplate: StringRedisTemplate
) {
    companion object {
        private const val BASE_URL = "https://api.epsonconnect.com"
    }

    private val logger = LoggerFactory.getLogger(EpsonConnectApi::class.java)

    fun authenticate(): String {
        return redisTemplate.opsForValue().get("accessToken") ?: run {
            logger.info("Authenticating with Epson Connect API")

            val request = Request.Builder()
                .post(epsonConnectProperties.authenticationRequest())
                .url("$BASE_URL/api/1/printing/oauth2/auth/token?subject=printer")
                .header(HttpHeaders.AUTHORIZATION, epsonConnectProperties.basicHeader())
                .build()

            epsonConnectClient.call(request)
                .bind(AuthenticationResponse::class.java)
                ?.run {
                    redisTemplate.opsForValue().set("deviceId", subjectId)
                    redisTemplate.opsForValue().set("accessToken", accessToken)
                    redisTemplate.expire("accessToken", expiresIn, TimeUnit.SECONDS)
                    logger.info("Authentication successful")

                    accessToken
                } ?: throw RuntimeException("Authentication failed")
        }
    }

    fun setPrintSettings(printSettings: PrintSettingsRequest): PrintSettingsResponse {
        logger.info("Setting print settings")
        val accessToken = authenticate()
        val deviceId = redisTemplate.opsForValue().get("deviceId")

        val request = Request.Builder()
            .post(printSettings.toRequestBody())
            .url("$BASE_URL/api/1/printing/printers/$deviceId/jobs")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .build()

        return epsonConnectClient.call(request)
            .bind(PrintSettingsResponse::class.java)
            ?.apply {
                logger.info("Created Job ID: $id")
                logger.info("Upload URI: $uploadUri")
            } ?: throw RuntimeException("Failed to set print settings")
    }

    fun uploadFileToPrint(
        uploadUri: String,
        fileData: ByteArray,
        contentType: String
    ): Boolean {
        logger.info("Uploading file to print")
        val binary = fileData.toRequestBody(contentType.toMediaType())
        val fileExtension = PrintOptions.FileExtension.fromContentType(contentType)

        val request = Request.Builder()
            .post(binary)
            .url("$uploadUri&File=1.$fileExtension")
            .build()

        logger.info("Uploaded file to Epson Connect successfully")
        return epsonConnectClient.call(request).isSuccessful
    }

    fun print(fileData: ByteArray, contentType: String, printSettings: PrintSettingsRequest): Boolean {
        val (jobId, uploadUri) = setPrintSettings(printSettings)
        uploadFileToPrint(uploadUri, fileData, contentType)

        logger.info("Executing print job: $jobId")
        val accessToken = authenticate()
        val deviceId = redisTemplate.opsForValue().get("deviceId")

        val request = Request.Builder()
            .post("".toRequestBody())
            .url("$BASE_URL/api/1/printing/printers/$deviceId/jobs/$jobId/print")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .build()

        return epsonConnectClient.call(request).isSuccessful
    }
}
