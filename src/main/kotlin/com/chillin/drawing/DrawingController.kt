package com.chillin.drawing

import com.chillin.epson.EpsonConnectService
import com.chillin.epson.request.PrintSettingsRequest
import com.chillin.drawing.request.ImageGenerationRequest
import com.chillin.drawing.request.ImagePrintRequest
import com.chillin.drawing.response.DrawingResponse
import com.chillin.drawing.response.DrawingResponseWrapper
import com.chillin.openai.DallEService
import com.chillin.s3.S3Service
import com.chillin.type.DrawingType
import com.chillin.type.MediaSubtype
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

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
        val pathname = "generated/${UUID.randomUUID()}.${MediaSubtype.JPEG.value}"

        val (url, revisedPrompt) = dallEService.generateImage(rawPrompt)
        val presignedUrl = s3Service.uploadImage(pathname, url, revisedPrompt)
        val savedImage = drawingService.save(pathname, DrawingType.GENERATED, rawPrompt, revisedPrompt)

        val responseBody = DrawingResponse(savedImage.drawingId, presignedUrl, rawPrompt)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody)
    }

    @PostMapping("/print")
    fun printDrawing(@RequestBody imagePrintRequest: ImagePrintRequest) {
        val (drawingId, _) = imagePrintRequest

        val pathname = drawingService.getNameById(drawingId)
        val fileData = s3Service.getImageData(pathname)

        val printSettings = PrintSettingsRequest(imagePrintRequest)
        epsonConnectService.print(fileData, printSettings)
    }

    @GetMapping
    fun getDrawings(@RequestParam type: DrawingType): DrawingResponseWrapper {
        val data = drawingService.getAllByType(type).map { drawing ->
            val url = s3Service.getImageUrl(drawing.pathname)
            DrawingResponse(drawing.drawingId, url, drawing.rawPrompt)
        }
        return DrawingResponseWrapper(type, data)
    }
}