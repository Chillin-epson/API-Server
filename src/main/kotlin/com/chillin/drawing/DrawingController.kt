package com.chillin.drawing

import com.chillin.openai.DallEService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/drawings")
class DrawingController(
    private val dallEService: DallEService,
) {
    @PostMapping("/gen")
    fun generateDrawing(@RequestBody imageGenerationRequest: ImageGenerationRequest): ResponseEntity<Unit> {
        dallEService.generateImage(imageGenerationRequest.prompt)
        return ResponseEntity.status(201).build()
    }
}