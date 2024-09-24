package com.chillin.auth.response

import org.slf4j.LoggerFactory
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.*


data class JWKSetResponse(
    val keys: List<JWK>
) {
    data class JWK(
        val kid: String,
        val e: String,
        val n: String,
        val alg: String,
        val kty: String,
        val use: String
    ) {
        fun toPublicKey(): PublicKey {
            logger.info("Converting JWK to PublicKey")

            val eBytes = Base64.getUrlDecoder().decode(e)
            val nBytes = Base64.getUrlDecoder().decode(n)

            val exp = BigInteger(SIGNUM_POSITIVE, eBytes)
            val mod = BigInteger(SIGNUM_POSITIVE, nBytes)

            val keySpec = RSAPublicKeySpec(mod, exp)
            val keyFactory = KeyFactory.getInstance(kty)

            return keyFactory.generatePublic(keySpec)
        }

        companion object {
            private const val SIGNUM_POSITIVE = 1
            private val logger = LoggerFactory.getLogger(JWK::class.java)
        }
    }
}
