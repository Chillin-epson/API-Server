package com.chillin.connect

import okhttp3.OkHttpClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(EpsonConnectProperties::class)
class EpsonConnectConfig {

    @Bean
    fun epsonConnectClient(): EpsonConnectClient {
        return EpsonConnectClient(OkHttpClient())
    }
}