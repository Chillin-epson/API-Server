package com.chillin.redis

object RedisKeyFactory {
    fun create(vararg keys: String): String {
        return keys.joinToString(":")
    }
}