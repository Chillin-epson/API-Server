package com.chillin.type

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType

enum class MediaSubtype(val value: String) {
    PDF("pdf"),
    JPEG("jpeg"),
    GIF("gif"),
    JSON("json"),
    FORM_DATA("form-data"),
    ALL("*");

    companion object {
        private val logger = LoggerFactory.getLogger(MediaSubtype::class.java)

        fun parse(contentTypeValue: String?): MediaSubtype {
            logger.info("Parsing media subtype from \"$contentTypeValue\"")
            return when (contentTypeValue?.substringAfterLast('.')) {
                PDF.value -> PDF
                JPEG.value -> JPEG
                GIF.value -> GIF
                JSON.value -> JSON
                ALL.value -> ALL
                MediaType.APPLICATION_PDF_VALUE -> PDF
                MediaType.IMAGE_JPEG_VALUE -> JPEG
                MediaType.IMAGE_GIF_VALUE -> GIF
                MediaType.APPLICATION_JSON_VALUE -> JSON
                MediaType.MULTIPART_FORM_DATA_VALUE -> FORM_DATA
                MediaType.ALL_VALUE -> ALL
                else -> throw IllegalArgumentException("Unsupported media type")
            }
        }
    }

    fun toMediaTypeValue(): String {
        return when (this) {
            PDF -> MediaType.APPLICATION_PDF_VALUE
            JPEG, ALL -> MediaType.IMAGE_JPEG_VALUE
            GIF -> MediaType.IMAGE_GIF_VALUE
            JSON -> MediaType.APPLICATION_JSON_VALUE
            FORM_DATA -> MediaType.MULTIPART_FORM_DATA_VALUE
        }
    }
}