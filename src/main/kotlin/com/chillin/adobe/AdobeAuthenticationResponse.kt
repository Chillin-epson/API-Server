package com.chillin.adobe

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class AdobeAuthenticationResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
)
