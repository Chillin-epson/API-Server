package com.chillin.epson.request

import com.chillin.drawing.request.ImagePrintRequest
import com.chillin.type.PrintMode
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.http.MediaType

@JsonInclude(Include.NON_NULL)
@JsonNaming(SnakeCaseStrategy::class)
data class PrintSettingsRequest(
    val jobName: String = "sample",
    val printMode: PrintMode = PrintMode.PHOTO,
    var printSetting: PrintSetting? = null
) {

    constructor(imagePrintRequest: ImagePrintRequest) : this() {
        this.printSetting = PrintSetting(
            mediaSize = imagePrintRequest.scale.toMediaSize()
        )
    }

    fun toRequestBody(): RequestBody {
        return jacksonObjectMapper()
            .writeValueAsString(this)
            .toRequestBody(MediaType.APPLICATION_JSON_VALUE.toMediaType())
    }
}


