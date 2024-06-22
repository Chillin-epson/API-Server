package com.chillin.motion.request

import com.fasterxml.jackson.annotation.JsonCreator

enum class MotionType(val value: String) {
    DANCE("dance"), HELLO("hello"), JUMP("jump"), ZOMBIE("zombie");

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromString(motionType: String): MotionType {
            return valueOf(motionType.uppercase())
        }
    }
}
