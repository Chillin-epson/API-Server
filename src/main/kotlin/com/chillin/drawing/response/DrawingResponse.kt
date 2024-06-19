package com.chillin.drawing.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_NULL)
data class DrawingResponse(
    val drawingId: Long,
    val url: String,
    val prompt: String? = null
)
