package com.chillin.adobe.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class AdobeAuthResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
)
