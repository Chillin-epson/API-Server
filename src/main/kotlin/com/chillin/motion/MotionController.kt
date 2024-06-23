package com.chillin.motion

import com.chillin.drawing.DrawingService
import com.chillin.drawing.response.DrawingResponse
import com.chillin.motion.request.MotionRequest
import com.chillin.s3.S3Service
import com.chillin.type.DrawingType
import com.chillin.type.MediaSubtype
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/motion")
class MotionController(
    private val drawingService: DrawingService,
    private val s3Service: S3Service,
    private val motionService: MotionService
) {
    @PostMapping("/{drawingId}")
    fun addMotion(@PathVariable drawingId: Long, @RequestBody request: MotionRequest): ResponseEntity<DrawingResponse> {
        val srcPathname = drawingService.getNameById(drawingId)
        val (jpegBytes, _) = s3Service.getImageData(srcPathname)
        val gifBytes = motionService.addMotion(srcPathname, jpegBytes, request.motionType)

        val targetPathname = "animated/${UUID.randomUUID()}.${MediaSubtype.GIF.value}"
        val (uploadedFilePathname, url) = s3Service.uploadImage(targetPathname, gifBytes)
        val savedImage = drawingService.save(uploadedFilePathname, DrawingType.ANIMATED)

        val response = DrawingResponse(savedImage.drawingId, url)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}