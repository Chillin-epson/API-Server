package com.chillin.redis

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
class RedisConfig {

    @Value("\${spring.data.redis.host}")
    private lateinit var host: String

    @Value("\${spring.data.redis.port}")
    private lateinit var port: String

    @Value("\${spring.data.redis.password}")
    private var password: String? = null

    @Bean
    fun lettuceConnectionFactory(): LettuceConnectionFactory {
        val config = RedisStandaloneConfiguration(host, port.toInt())
        config.password = RedisPassword.of(password)
        return LettuceConnectionFactory(config)
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): StringRedisTemplate {
        return StringRedisTemplate().apply {
            connectionFactory = redisConnectionFactory
        }
    }
}