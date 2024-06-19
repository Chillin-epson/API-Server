package com.chillin.drawing.request

import com.chillin.type.PrintScale

data class ImagePrintRequest(
    val drawingId: Long,
    var scale: PrintScale = PrintScale.LARGE
)