package com.chillin.connect.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.http.MediaType

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(SnakeCaseStrategy::class)
data class PrintSettingsRequest(
    val jobName: String = "sample",
    val printMode: String = PrintOptions.PrintMode.PHOTO,
    val printSetting: PrinterSetting? = null
) {
    fun toRequestBody(): RequestBody {
        return jacksonObjectMapper()
            .writeValueAsString(this)
            .toRequestBody(MediaType.APPLICATION_JSON_VALUE.toMediaType())
    }
}


