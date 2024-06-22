package com.chillin.adobe

import okhttp3.FormBody
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AdobePhotoshopProperties::class)
class AdobeConfig {

    @Bean
    fun authenticationForm(photoshopProperties: AdobePhotoshopProperties): FormBody {
        return photoshopProperties.authenticationForm()
    }
}