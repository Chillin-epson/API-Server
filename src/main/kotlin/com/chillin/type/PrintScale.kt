package com.chillin.type

enum class PrintScale {
    LARGE, MEDIUM, SMALL;

    fun toMediaSize(): MediaSize {
        return when (this) {
            LARGE -> MediaSize.MS_A4
            MEDIUM -> MediaSize.MS_B5
            SMALL -> MediaSize.MS_A5
        }
    }
}