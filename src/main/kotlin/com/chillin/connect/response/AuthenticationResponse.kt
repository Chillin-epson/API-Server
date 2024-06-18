package com.chillin.connect.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class AuthenticationResponse(
    val tokenType: String,
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String,
    val subjectType: String,
    val subjectId: String
)
