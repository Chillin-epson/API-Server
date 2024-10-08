package com.chillin.auth

import com.chillin.auth.response.TokenResponse
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtProvider: JwtProvider
) {
    fun issueToken(accountId: String): TokenResponse {
        val token = jwtProvider.issueToken(accountId)
        return TokenResponse(token, jwtProvider.expirationSeconds)
    }
}
