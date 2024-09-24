package com.chillin.auth.response

data class TokenResponse(
    val token: String,
    val expiresIn: Long,
    val grantType: String = "Bearer"
)
