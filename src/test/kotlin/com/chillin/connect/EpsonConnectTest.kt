package com.chillin.connect

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate

@SpringBootTest
class EpsonConnectTest(
    private val epsonConnectApi: EpsonConnectApi,
    private val redisTemplate: StringRedisTemplate
) : BehaviorSpec({

    given("인증을 한번도 하지 않았을 때") {
        `when`("인증을 시도해 성공하면") {
            val accessToken = epsonConnectApi.authenticate()
            then("레디스에 액세스 토큰이 저장되어야 한다.") {
                val redisAccessToken = redisTemplate.opsForValue().get("accessToken")

                redisAccessToken shouldNotBe null
                accessToken shouldBe redisAccessToken
            }
        }
    }

    given("인증을 한번 진행하고") {
        val accessToken = epsonConnectApi.authenticate()
        `when`("토큰이 만료된 후 인증을 한번 더 시도하면") {
            redisTemplate.opsForValue().getAndDelete("accessToken")
            epsonConnectApi.authenticate()
            then("만료된 토큰과 새로 발급된 토큰이 달라야 한다.") {
                val redisAccessToken = redisTemplate.opsForValue().get("accessToken")

                redisAccessToken shouldNotBe null
                accessToken shouldNotBe redisAccessToken
            }
        }
    }

    given("인증을 이미 했을 때") {
        `when`("토큰이 만료되지 않고 인증을 한번 더 시도하면") {
            val accessToken = epsonConnectApi.authenticate()
            then("두 토큰은 같아야 한다.") {
                accessToken shouldBe epsonConnectApi.authenticate()
            }
        }
    }
}) {
    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        val keys = redisTemplate.keys("*")
        redisTemplate.delete(keys)
    }
}