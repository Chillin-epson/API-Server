package com.chillin.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import okhttp3.FormBody
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*


@ConfigurationProperties(prefix = "custom.oauth2.apple")
data class AppleIdProperties(
    private val clientId: String,
    private val keyId: String,
    private val teamId: String,
    private val privateKey: String
) {

    fun verifyToken(idToken: String, publicKey: PublicKey): Claims {
        logger.info("Verifying identity token...")

        try {
            return Jwts.parser()
                .verifyWith(publicKey)
                .requireIssuer(BASE_URL)
                .requireAudience(clientId)
                .build()
                .parseSignedClaims(idToken)
                .payload
        } catch (e: JwtException) {
            throw RuntimeException("Failed to verify Apple ID token", e)
        }
    }

    fun toTokenFormBody(code: String): FormBody {
        return FormBody.Builder()
            .add("client_id", clientId)
            .add("client_secret", clientSecret())
            .add("code", code)
            .add("grant_type", "authorization_code")
            .build()
    }

    /**
     * Create a client secret for Apple ID authorization
     * @return A client secret
     * @see <a href="https://developer.apple.com/documentation/accountorganizationaldatasharing/creating-a-client-secret">Creating a client secret</a>
     */
    private fun clientSecret(): String {
        logger.info("Creating client secret...")

        val iat = Date()
        val exp = Date(iat.time).toInstant().plusSeconds(EXP_SECONDS)
        val privateKey = loadPrivateKey()

        return Jwts.builder()
            .header().keyId(keyId).and()
            .claims()
            .issuer(teamId)
            .issuedAt(iat)
            .expiration(Date(exp.toEpochMilli()))
            .audience().add(BASE_URL).and()
            .subject(clientId).and()
            .signWith(privateKey, Jwts.SIG.ES256)
            .compact()
    }

    private fun loadPrivateKey(): PrivateKey {
        logger.info("Loading private key...")

        val keyBytes = Base64.getDecoder().decode(privateKey)
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        return KeyFactory.getInstance(KEY_TYPE).generatePrivate(keySpec)
    }

    companion object {
        private const val BASE_URL = "https://appleid.apple.com"
        private const val KEY_TYPE = "EC"
        private const val EXP_SECONDS = 5 * 60L // 5 minutes
        private val logger = LoggerFactory.getLogger(AppleIdProperties::class.java)
    }
}