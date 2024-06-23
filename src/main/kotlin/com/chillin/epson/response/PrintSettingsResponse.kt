package com.chillin.epson.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class PrintSettingsResponse(
    val id: String,
    val uploadUri: String,
)