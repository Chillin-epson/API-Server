package com.chillin.drawing.response

data class ImageGenerationResponse(
    val drawingId: Long,
    val filename: String,
    val url: String,
)
