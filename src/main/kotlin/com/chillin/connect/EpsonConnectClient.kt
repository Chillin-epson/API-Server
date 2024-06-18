package com.chillin.connect

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus

class EpsonConnectClient(
    private val okHttpClient: OkHttpClient
) {

    private val logger = LoggerFactory.getLogger(EpsonConnectClient::class.java)

    fun call(request: Request): Response {
        logger.info("${request.method} ${request.url}")
        logger.info("Headers: ${request.headers.toMap()}")

        return okHttpClient.newCall(request)
            .execute()
            .apply(::handleResponse)
    }

    private fun handleResponse(response: Response) {
        takeUnless { response.isSuccessful }?.let {
            logger.error("${HttpStatus.valueOf(response.code)}: ${response.message}")
            throw RuntimeException("Request failed")
        }
        logger.info("${HttpStatus.valueOf(response.code)} ${response.protocol}")
    }
}

fun <T> Response.bind(clazz: Class<T>): T? {
    return body?.let { jacksonObjectMapper().readValue(it.string(), clazz) }
}
