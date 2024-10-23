package com.chillin.auth

import com.chillin.auth.response.TokenResponse
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import java.util.*

@ConfigurationProperties(prefix = "custom.jwt")
class JwtProvider(
    private val secretKey: String,
    private val issuer: String,

    @NestedConfigurationProperty
    private val expiration: TokenExpiration
) {

    fun issueToken(accountId: String): TokenResponse {
        val iat = Date()
        val sig = Keys.hmacShaKeyFor(secretKey.toByteArray())

        val accessToken = Jwts.builder()
            .claims()
            .subject(accountId)
            .issuer(issuer)
            .issuedAt(iat)
            .expiration(Date(iat.toInstant().plusSeconds(expiration.accessToken).toEpochMilli()))
            .and()
            .signWith(sig, Jwts.SIG.HS256)
            .compact()

        val refreshToken = Jwts.builder()
            .claims()
            .subject(accountId)
            .issuer(issuer)
            .issuedAt(iat)
            .expiration(Date(iat.toInstant().plusSeconds(expiration.refreshToken).toEpochMilli()))
            .and()
            .signWith(sig, Jwts.SIG.HS256)
            .compact()

        return TokenResponse(accessToken, refreshToken)
    }
}