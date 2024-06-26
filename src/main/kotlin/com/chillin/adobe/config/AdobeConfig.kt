package com.chillin.adobe.config

import okhttp3.FormBody
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AdobePhotoshopProperties::class)
class AdobeConfig {

    @Bean
    fun adobeAuthForm(photoshopProperties: AdobePhotoshopProperties): FormBody {
        return photoshopProperties.authenticationForm()
    }

    @Bean
    fun apiKeyHeader(photoshopProperties: AdobePhotoshopProperties): Pair<String, String> {
        return photoshopProperties.apiKeyHeader()
    }
}