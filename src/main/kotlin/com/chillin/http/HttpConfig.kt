package com.chillin.http

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HttpConfig {

    @Bean
    fun httpClient(): HttpClient {
        return HttpClient(OkHttpClient())
    }
}