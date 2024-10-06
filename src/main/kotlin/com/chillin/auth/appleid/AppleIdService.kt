package com.chillin.auth.appleid

import com.chillin.auth.response.AppleIdTokenResponse
import com.chillin.auth.response.JWKSetResponse
import com.chillin.http.HttpClient
import com.chillin.http.HttpClient.Companion.bind
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppleIdService(
    private val httpClient: HttpClient,
    private val provider: AppleIdProvider
) {

    fun verify(code: String): Pair<String, String> {
        val (idToken, refreshToken) = verifyCode(code)
        logger.info("Authorization code verified")

        val tokenKid = extractKid(idToken)
        logger.info("Extracted kid from idToken")

        val jwk = findMatchingJWK(tokenKid)
        logger.info("Matching JWK found")

        val publicKey = jwk.toPublicKey()
        logger.info("Converted JWK to public key")

        val accountId = provider.verifyToken(idToken, publicKey).subject
        logger.info("Identity token verified")

        return Pair<String, String>(accountId, refreshToken)
    }

    private fun verifyCode(code: String): AppleIdTokenResponse {
        logger.info("Verifying code...")

        val formBody = provider.createTokenFormBody(code)
        val request = Request.Builder()
            .post(formBody)
            .url("$BASE_URL/auth/oauth2/v2/token")
            .build()

        return httpClient.call(request)
            .bind(AppleIdTokenResponse::class.java)
            ?: throw RuntimeException("Failed to issue Apple ID token")
    }

    private fun extractKid(idToken: String): String {
        logger.info("Extracting kid from idToken...")
        val header = idToken.split(".").first()
        val rawContent = Base64.getUrlDecoder().decode(header).toString(Charsets.UTF_8)

        return ObjectMapper().readValue(rawContent, Map::class.java).let {
            it["kid"] as String
        }
    }

    private fun findMatchingJWK(tokenKid: String): JWKSetResponse.JWK {
        logger.info("Finding matching JWK for kid...")

        return getJWKs().find {
            it.kid == tokenKid
        } ?: throw RuntimeException("Failed to find matching JWK")
    }

    private fun getJWKs(): List<JWKSetResponse.JWK> {
        logger.info("Fetching JWKs from Apple ID server...")

        val request = Request.Builder()
            .get()
            .url("$BASE_URL/auth/oauth2/v2/keys")
            .build()

        return httpClient.call(request)
            .bind(JWKSetResponse::class.java)?.keys
            ?: throw RuntimeException("Failed to get JWKs")
    }

    companion object {
        private const val BASE_URL = "https://appleid.apple.com"
        private val logger = LoggerFactory.getLogger(AppleIdService::class.java)
    }
}