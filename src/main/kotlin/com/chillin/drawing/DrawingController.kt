package com.chillin.drawing

import com.chillin.drawing.domain.DrawingType
import com.chillin.openai.DallEService
import com.chillin.s3.S3Service
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/drawings")
class DrawingController(
    private val dallEService: DallEService,
    private val s3Service: S3Service,
    private val drawingService: DrawingService
) {
    @PostMapping("/gen")
    fun generateDrawing(@RequestBody imageGenerationRequest: ImageGenerationRequest): ResponseEntity<Unit> {
        val filename = "generative/${UUID.randomUUID()}.jpeg"
        val rawPrompt = imageGenerationRequest.prompt
        val (url, revisedPrompt) = dallEService.generateImage(rawPrompt)

        val presignedUrl = s3Service.uploadImage(filename, url)
        println("Presigned URL: $presignedUrl")

        drawingService.save(filename, DrawingType.GENERATED, rawPrompt, revisedPrompt)

        return ResponseEntity.status(201).build()
    }
}