package com.chillin.adobe

import com.chillin.adobe.request.CutoutRequest
import com.chillin.adobe.response.AdobeAuthenticationResponse
import com.chillin.http.HttpClient
import com.chillin.http.HttpClient.Companion.bind
import com.chillin.redis.RedisKeyFactory
import okhttp3.FormBody
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class AdobeService(
    private val httpClient: HttpClient,
    private val redisTemplate: StringRedisTemplate,
    private val authenticationForm: FormBody,
    private val apiKeyHeader: Pair<String, String>
) {

    fun authenticate(): String {
        val accessTokenKeyName = RedisKeyFactory.create("adobe", "photoshop", "access-token")

        return redisTemplate.opsForValue().get(accessTokenKeyName) ?: run {
            logger.info("Authenticating with Adobe Photoshop API")

            val request = Request.Builder()
                .post(authenticationForm)
                .url(AUTH_URL)
                .build()

            return httpClient.call(request)
                .bind(AdobeAuthenticationResponse::class.java)
                ?.run {
                    redisTemplate.opsForValue().set(accessTokenKeyName, accessToken)
                    redisTemplate.expire(accessTokenKeyName, expiresIn, TimeUnit.SECONDS)

                    logger.info("Saved access token to Redis, setting expiration time to $expiresIn seconds.")
                    logger.info("Authentication successful.")

                    accessToken
                } ?: throw RuntimeException("Authentication failed")
        }
    }

    fun cutout(srcUrl: String, dstUrl: String): Boolean {
        logger.info("Requesting background removal to Adobe Photoshop API")

        val accessToken = authenticate()
        val cutoutRequest = CutoutRequest(srcUrl, dstUrl)
        val request = Request.Builder()
            .post(cutoutRequest.toRequestBody())
            .url(CUTOUT_URL)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .header(apiKeyHeader.first, apiKeyHeader.second)
            .build()

        val response = httpClient.call(request)
        return response.apply {
            if (isSuccessful) logger.info("Background removal request successful")
            else logger.error("Background removal request failed: $response")
        }.isSuccessful
    }

    companion object {
        private const val AUTH_URL = "https://ims-na1.adobelogin.com/ims/token/v3"
        private const val CUTOUT_URL = "https://image.adobe.io/sensei/cutout"
        private val logger = LoggerFactory.getLogger(AdobeService::class.java)
    }
}