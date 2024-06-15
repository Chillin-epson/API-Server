package com.chillin.openai

import org.springframework.ai.image.ImagePrompt
import org.springframework.ai.openai.OpenAiImageModel
import org.springframework.ai.openai.api.OpenAiImageApi
import org.springframework.stereotype.Service
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.util.*

@Service
class DallEService(
    private val openAiImageApi: OpenAiImageApi,
    private val imageBasePath: String,
    private val instructionPrefix: String,
    private val instructionPostfix: String,
) {
    fun generateImage(instruction: String) {
        val prompt = ImagePrompt("$instructionPrefix $instruction $instructionPostfix")
        val response = OpenAiImageModel(openAiImageApi).call(prompt)
        val imageUrl = response.result.output.url

        URL(imageUrl).openStream().use { input ->
            val path = File(imageBasePath + "${UUID.randomUUID()}.jpeg").toPath()
            Files.copy(input, path)
        }
    }
}