package com.chillin.type

import com.fasterxml.jackson.annotation.JsonValue

/**
 * Supported print options for Epson L6490
 */
interface PrintOption {
    @get:JsonValue
    val value: String
}

enum class PrintMode(override val value: String) : PrintOption {
    DOCUMENT("document"),
    PHOTO("photo")
}

enum class MediaSize(override val value: String) : PrintOption {
    MS_LEGAL("ms_legal"),     // mt_plainpaper only
    MS_LETTER("ms_letter"),   // mt_plainpaper only
    MS_A4("ms_a4"),           // mt_plainpaper, mt_photopaper
    MS_B5("ms_b5"),           // mt_plainpaper only
    MS_A5("ms_a5"),           // mt_plainpaper only
    MS_A6("ms_a6"),           // mt_plainpaper only
    MS_2L("ms_2l"),           // mt_photopaper only
    MS_KG("ms_kg"),           // mt_photopaper only
    MS_L("ms_l")              // mt_photopaper only
}

enum class MediaType(override val value: String) : PrintOption {
    MT_PLAINPAPER("mt_plainpaper"),
    MT_PHOTOPAPER("mt_photopaper")
}


enum class PrintQuality(override val value: String) : PrintOption {
    HIGH("high"),
    NORMAL("normal")
}

enum class Source(override val value: String) : PrintOption {
    AUTO("auto"),
    FRONT1("front1")
}

enum class ColorMode(override val value: String) : PrintOption {
    COLOR("color"),
    MONO("mono")
}
