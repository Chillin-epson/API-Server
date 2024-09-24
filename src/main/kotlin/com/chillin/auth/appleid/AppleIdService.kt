package com.chillin.auth.appleid

import com.chillin.auth.response.AppleIdTokenResponse
import com.chillin.auth.response.JWKSetResponse
import com.chillin.http.HttpClient
import com.chillin.http.HttpClient.Companion.bind
import io.jsonwebtoken.Jwts
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AppleIdService(
    private val httpClient: HttpClient,
    private val provider: AppleIdProvider
) {

    fun verify(code: String): Pair<String, String> {
        val (idToken, refreshToken) = verifyCode(code)
        logger.info("Authorization code verified, idToken=$idToken, refreshToken=$refreshToken")

        val tokenKid = extractKid(idToken)
        logger.info("Extracted kid=$tokenKid from idToken")

        val jwk = findMatchingJWK(tokenKid)
        logger.info("Found matching JWK=$jwk")

        val publicKey = jwk.toPublicKey()
        logger.info("Converted JWK to public key=$publicKey")

        val accountId = provider.verifyToken(idToken, publicKey).subject
        logger.info("Identity token verified, accountId=$accountId")

        return Pair<String, String>(accountId, refreshToken)
    }

    private fun verifyCode(code: String): AppleIdTokenResponse {
        logger.info("Verifying code=$code")

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
        logger.info("Extracting kid from idToken=$idToken")

        return Jwts.parser().build()
            .parseSignedClaims(idToken)
            .header
            .keyId
    }

    private fun findMatchingJWK(tokenKid: String): JWKSetResponse.JWK {
        logger.info("Finding matching JWK for tokenKid=$tokenKid")

        return getJWKs().find {
            it.kid == tokenKid
        } ?: throw RuntimeException("Failed to find matching JWK")
    }

    private fun getJWKs(): List<JWKSetResponse.JWK> {
        logger.info("Fetching JWKs from Apple ID server")

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