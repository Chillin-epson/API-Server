package com.chillin.adobe.config

import okhttp3.FormBody
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "custom.adobe.photoshop")
data class AdobePhotoshopProperties(
    private val clientId: String,
    private val clientSecret: String,
    private val scope: String
) {
    fun authenticationForm(): FormBody {
        return FormBody.Builder()
            .add("client_id", clientId)
            .add("client_secret", clientSecret)
            .add("scope", scope)
            .add("grant_type", "client_credentials")
            .build()
    }

    fun apiKeyHeader(): Pair<String, String> {
        return Pair("x-api-key", clientId)
    }
}
