package com.chillin.connect.request

/**
 * Supported print options for Epson L6490
 */
object PrintOptions {
    object PrintMode {
        const val DOCUMENT = "document"
        const val PHOTO = "photo"
    }

    object MediaSize {
        const val MS_LEGAL = "ms_legal"     // mt_plainpaper only
        const val MS_LETTER = "ms_letter"   // mt_plainpaper only
        const val MS_A4 = "ms_a4"           // mt_plainpaper, mt_photopaper
        const val MS_B5 = "ms_b5"           // mt_plainpaper only
        const val MS_A5 = "ms_a5"           // mt_plainpaper only
        const val MS_A6 = "ms_a6"           // mt_plainpaper only
        const val MS_2L = "ms_2l"           // mt_photopaper only
        const val MS_KG = "ms_kg"           // mt_photopaper only
        const val MS_L = "ms_l"             // mt_photopaper only
    }

    object MediaType {
        const val MT_PLAINPAPER = "mt_plainpaper"
        const val MT_PHOTOPAPER = "mt_photopaper"
    }

    object PrintQuality {
        const val HIGH = "high"
        const val NORMAL = "normal"
    }

    object Source {
        const val AUTO = "auto"
        const val FRONT1 = "front1"
    }

    object ColorMode {
        const val COLOR = "color"
        const val MONO = "mono"
    }
}
