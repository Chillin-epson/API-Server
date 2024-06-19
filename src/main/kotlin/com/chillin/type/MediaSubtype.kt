package com.chillin.type

import org.springframework.http.MediaType

enum class MediaSubtype(val value: String) {
    PDF("pdf"),
    JPEG("jpeg"),
    JSON("json"),
    FORM_DATA("form-data");

    companion object {
        fun parse(contentTypeValue: String?): MediaSubtype {
            return when (contentTypeValue?.substringAfterLast('.')) {
                PDF.value -> PDF
                JPEG.value -> JPEG
                JSON.value -> JSON
                MediaType.APPLICATION_PDF_VALUE -> PDF
                MediaType.IMAGE_JPEG_VALUE -> JPEG
                MediaType.APPLICATION_JSON_VALUE -> JSON
                MediaType.MULTIPART_FORM_DATA_VALUE -> FORM_DATA
                else -> throw IllegalArgumentException("Unsupported media type")
            }
        }
    }

    fun toMediaTypeValue(): String {
        return when (this) {
            PDF -> MediaType.APPLICATION_PDF_VALUE
            JPEG -> MediaType.IMAGE_JPEG_VALUE
            JSON -> MediaType.APPLICATION_JSON_VALUE
            FORM_DATA -> MediaType.MULTIPART_FORM_DATA_VALUE
        }
    }
}