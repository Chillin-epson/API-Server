package com.chillin.connect

import com.chillin.connect.request.AuthenticationRequest
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

    fun authenticationRequest(): FormBody {
        return AuthenticationRequest(printerAddress).toFormBody()
    }
}