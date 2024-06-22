package com.chillin.drawing

import com.chillin.drawing.domain.Drawing
import com.chillin.type.DrawingType
import org.springframework.stereotype.Service

@Service
class DrawingService(
    private val drawingRepository: DrawingRepository
) {
    fun save(
        pathname: String,
        drawingType: DrawingType,
        rawPrompt: String? = null,
        revisedPrompt: String? = null
    ): Drawing {
        val drawing = Drawing(pathname, drawingType, rawPrompt, revisedPrompt)
        return drawingRepository.save(drawing)
    }

    fun getNameById(drawingId: Long): String {
        return drawingRepository.findById(drawingId)
            .orElseThrow { NoSuchElementException("Drawing not found") }
            .pathname
    }

    fun getAllByType(type: DrawingType) = drawingRepository.findAllByTypeOrderByCreatedAtDesc(type)
}