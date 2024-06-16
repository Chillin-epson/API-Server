package com.chillin.drawing

import com.chillin.drawing.domain.Drawing
import com.chillin.drawing.domain.DrawingType
import org.springframework.stereotype.Service

@Service
class DrawingService(
    private val drawingRepository: DrawingRepository
) {
    fun save(filename: String, drawingType: DrawingType, rawPrompt: String? = null, revisedPrompt: String? = null) {
        val drawing = Drawing(filename, drawingType, rawPrompt, revisedPrompt)
        drawingRepository.save(drawing)
    }
}