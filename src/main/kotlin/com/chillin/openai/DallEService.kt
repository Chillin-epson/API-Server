package com.chillin.openai

import org.springframework.ai.autoconfigure.openai.OpenAiImageProperties
import org.springframework.ai.image.ImagePrompt
import org.springframework.ai.openai.OpenAiImageModel
import org.springframework.ai.openai.api.OpenAiImageApi
import org.springframework.ai.openai.metadata.OpenAiImageGenerationMetadata
import org.springframework.ai.retry.RetryUtils
import org.springframework.stereotype.Service

@Service
class DallEService(
    private val openAiImageProperties: OpenAiImageProperties,
    private val openAiImageApi: OpenAiImageApi,
    private val instructionPrefix: String,
    private val instructionPostfix: String,
) {
    fun generateImage(instruction: String): Pair<String, String> {
        val prompt = ImagePrompt("$instructionPrefix $instruction $instructionPostfix")
        val result = OpenAiImageModel(
            openAiImageApi,
            openAiImageProperties.options,
            RetryUtils.DEFAULT_RETRY_TEMPLATE
        ).call(prompt).result

        val url = result.output.url
        val metadata = result.metadata as OpenAiImageGenerationMetadata

        return Pair(url, metadata.revisedPrompt)
    }
}