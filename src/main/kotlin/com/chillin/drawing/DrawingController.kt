package com.chillin.drawing

import com.chillin.connect.EpsonConnectService
import com.chillin.connect.request.PrintSettingsRequest
import com.chillin.drawing.request.ImageGenerationRequest
import com.chillin.drawing.request.ImagePrintRequest
import com.chillin.drawing.response.DrawingResponse
import com.chillin.drawing.response.DrawingResponseWrapper
import com.chillin.openai.DallEService
import com.chillin.s3.S3Service
import com.chillin.type.DrawingType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/drawings")
class DrawingController(
    private val dallEService: DallEService,
    private val s3Service: S3Service,
    private val drawingService: DrawingService,
    private val epsonConnectService: EpsonConnectService
) {
    @PostMapping("/gen")
    fun generateDrawing(@RequestBody imageGenerationRequest: ImageGenerationRequest): ResponseEntity<DrawingResponse> {
        val rawPrompt = imageGenerationRequest.prompt
        val (url, revisedPrompt) = dallEService.generateImage(rawPrompt)
        val (filename, presignedUrl) = s3Service.uploadImage(url, revisedPrompt)
        val savedImage = drawingService.save(filename, DrawingType.GENERATED, rawPrompt, revisedPrompt)
        val responseBody = DrawingResponse(savedImage.drawingId, presignedUrl, rawPrompt)

        return ResponseEntity.status(201).body(responseBody)
    }

    @PostMapping("/print")
    fun printDrawing(@RequestBody imagePrintRequest: ImagePrintRequest) {
        val (drawingId, _) = imagePrintRequest
        val filename = drawingService.getNameById(drawingId)
        val fileData = s3Service.getImageData(filename)
        val printSettings = PrintSettingsRequest(imagePrintRequest)

        epsonConnectService.print(fileData, printSettings)
    }

    @GetMapping
    fun getDrawings(@RequestParam type: DrawingType): DrawingResponseWrapper {
        val data = drawingService.getAllByType(type).map { drawing ->
            val url = s3Service.getImageUrl(drawing.filename)
            DrawingResponse(drawing.drawingId, url, drawing.rawPrompt)
        }
        return DrawingResponseWrapper(type, data)
    }
}