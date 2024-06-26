package com.chillin.adobe

import com.chillin.redis.RedisKeyFactory
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate

@SpringBootTest
class AdobeServiceTest(
    private val adobeService: AdobeService,
    private val redisTemplate: StringRedisTemplate
) : BehaviorSpec({
    val accessTokenName = RedisKeyFactory.create("adobe", "photoshop", "access-token")

    given("액세스 토큰이 없을 때") {
        `when`("액세스 토큰을 요청하면") {
            val token = adobeService.authenticate()
            then("액세스 토큰을 반환한다") {
                redisTemplate.opsForValue().get(accessTokenName) shouldBe token
            }
        }
    }
})