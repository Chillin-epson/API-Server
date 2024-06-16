package com.chillin.openai

import org.springframework.ai.autoconfigure.openai.OpenAiImageProperties
import org.springframework.ai.openai.api.OpenAiImageApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenAiConfig {

    @Value("\${custom.image.instruction.prefix}")
    private lateinit var instructionPrefix: String

    @Value("\${custom.image.instruction.postfix}")
    private lateinit var instructionPostfix: String

    @Bean
    fun openAiImageApi(openAiImageProperties: OpenAiImageProperties): OpenAiImageApi {
        return OpenAiImageApi(openAiImageProperties.apiKey)
    }

    @Bean
    fun instructionPrefix(): String {
        return instructionPrefix
    }

    @Bean
    fun instructionPostfix(): String {
        return instructionPostfix
    }
}
