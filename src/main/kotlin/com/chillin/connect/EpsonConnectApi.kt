package com.chillin.connect

import com.chillin.connect.response.AuthenticationResponse
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE
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
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
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
}
