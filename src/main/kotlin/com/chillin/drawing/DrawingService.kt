package com.chillin.drawing

import com.chillin.drawing.domain.Drawing
import com.chillin.type.DrawingType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DrawingService(
    private val drawingRepository: DrawingRepository
) {
    private val logger = LoggerFactory.getLogger(DrawingService::class.java)

    fun save(
        pathname: String,
        drawingType: DrawingType,
        rawPrompt: String? = null,
        revisedPrompt: String? = null
    ): Drawing {
        logger.info("Saving drawing to db...")
        val drawing = Drawing(pathname, drawingType, rawPrompt, revisedPrompt)

        return drawingRepository.save(drawing).apply {
            logger.info("Saved drawing to db: drawingId=${drawingId}, pathname=$pathname, drawingType=$drawingType, rawPrompt=$rawPrompt, revisedPrompt=$revisedPrompt")
        }
    }

    fun getNameById(drawingId: Long): String {
        logger.info("Querying drawing by ID: drawingId=$drawingId...")

        return drawingRepository.findById(drawingId)
            .orElseThrow { NoSuchElementException("Drawing not found") }
            .let {
                logger.info("Found drawing: pathname=\"${it.pathname}\"")
                it.pathname
            }
    }

    fun getAllByType(type: DrawingType) = drawingRepository.findAllByTypeOrderByCreatedAtDesc(type)
}