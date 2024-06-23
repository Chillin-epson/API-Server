package com.chillin.adobe.request

import com.chillin.type.StorageType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.http.MediaType


data class AdobeCutoutRequest(
    val input: Input,
    val output: Output
) {
    constructor(inputHref: String, outputHref: String) : this(Input(inputHref), Output(outputHref))

    class Input(val href: String, val storage: StorageType = StorageType.EXTERNAL)
    class Output(val href: String, val storage: StorageType = StorageType.EXTERNAL)

    fun toRequestBody(): RequestBody {
        return jacksonObjectMapper()
            .writeValueAsString(this)
            .toRequestBody(MediaType.APPLICATION_JSON_VALUE.toMediaType())
    }
}