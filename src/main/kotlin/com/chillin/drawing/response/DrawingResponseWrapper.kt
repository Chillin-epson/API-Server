package com.chillin.drawing.response

import com.chillin.type.DrawingType

data class DrawingResponseWrapper(
    val type: DrawingType,
    val data: List<DrawingResponse>
)