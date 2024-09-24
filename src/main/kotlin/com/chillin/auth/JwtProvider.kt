package com.chillin.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.*

@ConfigurationProperties(prefix = "custom.jwt")
class JwtProvider(
    private val secretKey: String,
    private val issuer: String,
    val expirationSeconds: Long
) {

    fun issueToken(accountId: String): String {
        val iat = Date()
        val exp = Date(iat.toInstant().plusSeconds(expirationSeconds).toEpochMilli())
        val sig = Keys.hmacShaKeyFor(secretKey.toByteArray())

        return Jwts.builder()
            .claims()
            .subject(accountId)
            .issuer(issuer)
            .issuedAt(iat)
            .expiration(exp)
            .and()
            .signWith(sig, Jwts.SIG.HS256)
            .compact()
    }
}