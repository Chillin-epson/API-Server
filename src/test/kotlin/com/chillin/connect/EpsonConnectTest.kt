package com.chillin.connect

import com.chillin.connect.request.PrintSettingsRequest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.MediaType

@SpringBootTest
class EpsonConnectTest(
    private val epsonConnectService: EpsonConnectService,
    private val redisTemplate: StringRedisTemplate
) : BehaviorSpec({

    given("인증을 한번도 하지 않았을 때") {
        `when`("인증을 시도해 성공하면") {
            val accessToken = epsonConnectService.authenticate()

            then("레디스에 액세스 토큰이 저장되어야 한다.") {
                val redisAccessToken = redisTemplate.opsForValue().get("accessToken")

                redisAccessToken shouldNotBe null
                accessToken shouldBe redisAccessToken
            }
        }
    }

    given("인증을 한번 진행하고") {
        val accessToken = epsonConnectService.authenticate()
        `when`("토큰이 만료된 후 인증을 한번 더 시도하면") {
            redisTemplate.opsForValue().getAndDelete("accessToken")
            epsonConnectService.authenticate()

            then("만료된 토큰과 새로 발급된 토큰이 달라야 한다.") {
                val redisAccessToken = redisTemplate.opsForValue().get("accessToken")

                redisAccessToken shouldNotBe null
                accessToken shouldNotBe redisAccessToken
            }
        }
    }

    given("인증을 이미 했을 때") {
        `when`("토큰이 만료되지 않고 인증을 한번 더 시도하면") {
            val accessToken = epsonConnectService.authenticate()

            then("두 토큰은 같아야 한다.") {
                accessToken shouldBe epsonConnectService.authenticate()
            }
        }
    }

    given("기본 프린트 설정으로") {
        val defaultSettings = PrintSettingsRequest()

        `when`("값을 설정하면") {
            val response = epsonConnectService.setPrintSettings(defaultSettings)

            then("Job ID와 업로드 URI가 생성되어야 한다.") {
                response.id shouldNotBe ""
                response.uploadUri shouldNotBe ""
            }
        }
    }

    given("프린트 설정한 뒤") {
        val defaultSettings = PrintSettingsRequest()
        val (_, uploadUri) = epsonConnectService.setPrintSettings(defaultSettings)

        `when`("업로드를 시도하면") {
            val fileData = Pair(ByteArray(0), MediaType.IMAGE_JPEG_VALUE)
            val isSuccessful = epsonConnectService.uploadFileToPrint(uploadUri, fileData)

            then("업로드가 성공해야 한다.") {
                isSuccessful shouldBe true
            }
        }
    }

    given("프린트할 파일이 있을 때") {
        val fileData = Pair(ByteArray(0), MediaType.IMAGE_JPEG_VALUE)
        // or use File("path/to/document").readBytes() instead of ByteArray(0) to print an actual document

        `when`("프린트를 시도하면") {
            val printSettings = PrintSettingsRequest()
            val isSuccessful = epsonConnectService.print(fileData, printSettings)

            then("프린트가 성공해야 한다.") {
                isSuccessful shouldBe true
            }
        }
    }
}) {
    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        val keys = redisTemplate.keys("*")
        redisTemplate.delete(keys)
    }
}