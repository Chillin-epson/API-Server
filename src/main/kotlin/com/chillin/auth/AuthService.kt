package com.chillin.auth

import com.chillin.auth.response.TokenResponse
import com.chillin.redis.RedisKeyFactory
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtProvider: JwtProvider,
    private val redisTemplate: StringRedisTemplate
) {
    fun issueToken(accountId: String): TokenResponse {
        logger.info("Issuing token...")
        val tokens = jwtProvider.issueToken(accountId)

        logger.info("Saving refresh token to redis...")
        val tokenKeyName = RedisKeyFactory.create(accountId, "refresh-token")
        redisTemplate.opsForValue().set(tokenKeyName, tokens.refreshToken)

        return tokens
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthService::class.java)
    }
}
