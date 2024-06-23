package com.chillin.epson.config

import okhttp3.FormBody
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.http.HttpHeaders
import java.nio.charset.Charset

@ConfigurationProperties(prefix = "custom.epson-connect")
class EpsonConnectProperties(
    private val printerAddress: String,
    private val clientId: String,
    private val clientSecret: String,
) {

    fun basicHeader(charset: Charset? = null): String {
        return "Basic ${HttpHeaders.encodeBasicAuth(clientId, clientSecret, charset)}"
    }

    fun authenticationForm(): FormBody {
        return FormBody.Builder()
            .add("grant_type", "password")
            .add("username", printerAddress)
            .add("password", "")
            .build()
    }
}