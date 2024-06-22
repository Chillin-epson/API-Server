package com.chillin.connect

import com.chillin.http.HttpClient
import okhttp3.OkHttpClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(EpsonConnectProperties::class)
class EpsonConnectConfig {

    @Bean
    fun epsonConnectClient(): HttpClient {
        return HttpClient(OkHttpClient())
    }
}