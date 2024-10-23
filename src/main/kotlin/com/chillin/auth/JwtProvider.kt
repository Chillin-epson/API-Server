package com.chillin.auth

import com.chillin.auth.response.TokenResponse
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import java.util.*

@ConfigurationProperties(prefix = "custom.jwt")
class JwtProvider(
    secretKey: String,
    private val issuer: String,

    @NestedConfigurationProperty
    private val expiration: TokenExpiration
) {

    private val sig = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun issueToken(accountId: String): TokenResponse {
        val iat = Date()

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

    fun validate(token: String): Claims {
        try {
            return Jwts.parser()
                .verifyWith(sig)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: JwtException) {
            throw JwtException("Failed to verify token", e)
        }
    }
}