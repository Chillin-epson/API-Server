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

    fun reissueToken(token: String): TokenResponse? {
        logger.info("Validating token...")
        val payload = jwtProvider.validate(token)
        val accountId = payload.subject

        val tokenKeyName = RedisKeyFactory.create(accountId, "refresh-token")
        val storedToken =
            redisTemplate.opsForValue().get(tokenKeyName) ?: return null // if token is already invalidated

        return if (isReuseDetected(token, storedToken)) invalidateToken(tokenKeyName)
        else issueToken(accountId)
    }

    private fun isReuseDetected(token: String, storedToken: String): Boolean {
        logger.info("Checking if token is reused...")
        return token != storedToken
    }

    private fun invalidateToken(tokenKeyName: String): TokenResponse? {
        logger.info("Invalidating token...")
        redisTemplate.delete(tokenKeyName)
        return null
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthService::class.java)
    }
}