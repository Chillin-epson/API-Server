package com.chillin.connect

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.*

class EpsonConnectClient(
    private val okHttpClient: OkHttpClient
) {
    companion object {
        private val logger = LoggerFactory.getLogger(EpsonConnectClient::class.java)
        private fun RequestBody.content(): String {
            val contentType = contentType()?.toString()
            logger.info("Content-Type: {${contentType ?: "none"}}")

            return when (contentType?.substringBefore(";")) {
                IMAGE_JPEG_VALUE, APPLICATION_PDF_VALUE -> "{binary data}"
                APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE, APPLICATION_FORM_URLENCODED_VALUE ->
                    okio.Buffer().let {
                        writeTo(it)
                        it.readUtf8()
                    }

                else -> {
                    if (contentType != null) {
                        logger.warn("Couldn't resolve this type $contentType. Add a new case in RequestBody.content() method.")
                    }
                    "{}"
                }
            }
        }

        fun <T> Response.bind(clazz: Class<T>): T? {
            return body?.let {
                val responseBody = it.string()
                logger.info("Response: $responseBody")
                jacksonObjectMapper().readValue(responseBody, clazz)
            }
        }
    }

    fun call(request: Request): Response {
        logger.info("${request.method} ${request.url}")
        logger.info("Headers: ${request.headers.toMap()}")
        logger.info("Body: ${request.body?.content() ?: "{}"}")

        return okHttpClient.newCall(request)
            .execute()
            .apply(::handleResponse)
    }

    private fun handleResponse(response: Response) {
        if (response.isSuccessful.not()) {
            logger.error("${HttpStatus.valueOf(response.code)}")
            throw RuntimeException("Request failed")
        }
        logger.info("${HttpStatus.valueOf(response.code)} ${response.protocol}")
    }
}
