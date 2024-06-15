package com.chillin.openai

import org.springframework.ai.openai.api.OpenAiImageApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenAiConfig {

    @Value("\${spring.ai.openai.api-key}")
    private lateinit var apiKey: String

    @Value("\${custom.image.path}")
    private lateinit var imageBasePath: String

    @Value("\${custom.image.instruction.prefix}")
    private lateinit var instructionPrefix: String

    @Value("\${custom.image.instruction.postfix}")
    private lateinit var instructionPostfix: String

    @Bean
    fun openAiImageApi(): OpenAiImageApi {
        return OpenAiImageApi(apiKey)
    }

    @Bean
    fun instructionPrefix(): String {
        return instructionPrefix
    }

    @Bean
    fun instructionPostfix(): String {
        return instructionPostfix
    }

    @Bean
    fun imageBasePath(): String {
        return imageBasePath
    }
}
