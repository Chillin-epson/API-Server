package com.chillin.connect.request

import com.chillin.type.ColorMode
import com.chillin.type.MediaSize
import com.chillin.type.MediaType
import com.chillin.type.PrintQuality
import com.chillin.type.Source
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonInclude(Include.NON_NULL)
@JsonNaming(SnakeCaseStrategy::class)
data class PrintSetting(
    val mediaSize: MediaSize = MediaSize.MS_A4,
    val mediaType: MediaType = MediaType.MT_PLAINPAPER,
    val borderless: Boolean = false,
    val printQuality: PrintQuality = PrintQuality.NORMAL,
    val source: Source = Source.AUTO,
    val colorMode: ColorMode = ColorMode.MONO,
) {
    @JsonProperty("2_sided")
    val twoSided: String? = null
    val reverseOrder: Boolean? = null
    val copies: Int? = null
    val collate: Boolean? = null
}