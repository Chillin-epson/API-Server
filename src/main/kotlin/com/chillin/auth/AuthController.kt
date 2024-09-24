package com.chillin.auth

import com.chillin.auth.request.SignInWithAppleRequest
import com.chillin.member.MemberService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/auth")
class AuthController(
    private val appleIdService: AppleIdService,
    private val memberService: MemberService
) {
    @PostMapping("/oauth2/token")
    fun signUpWithApple(@RequestBody request: SignInWithAppleRequest): String {
        val (accountId, refreshToken) = appleIdService.verify(request.code)
        memberService.register(accountId, refreshToken)
        return "It works!"
    }
}