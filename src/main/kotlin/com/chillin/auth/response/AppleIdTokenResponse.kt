package com.chillin.auth.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class AppleIdTokenResponse(
    val idToken: String,
    val refreshToken: String,
    val accessToken: String,
    val expiresIn: Int,
    val tokenType: String
)