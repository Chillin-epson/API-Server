package com.chillin.connect.request

import com.fasterxml.jackson.annotation.JsonProperty

data class PrinterSetting(
    val mediaSize: String = PrintOptions.MediaSize.MS_A4,
    val mediaType: String = PrintOptions.MediaType.MT_PLAINPAPER,
    val borderless: Boolean = false,
    val printQuality: String = PrintOptions.PrintQuality.NORMAL,
    val source: String = PrintOptions.Source.AUTO,
    val colorMode: String = PrintOptions.ColorMode.MONO,
) {
    @JsonProperty("2_sided")
    val twoSided: String? = null
    val reverseOrder: Boolean? = null
    val copies: Int? = null
    val collate: Boolean? = null
}