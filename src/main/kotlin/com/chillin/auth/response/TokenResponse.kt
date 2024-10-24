package com.chillin.auth.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val grantType: String = "Bearer"
)
