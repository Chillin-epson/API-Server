package com.chillin.drawing.request

import com.chillin.type.PrintScale

data class ImagePrintRequest(
    val drawingId: Long,
    val printScale: PrintScale
) {
}