package com.chillin.auth

import com.chillin.auth.appleid.AppleIdService
import com.chillin.auth.request.SignInWithAppleRequest
import com.chillin.auth.response.TokenResponse
import com.chillin.member.MemberService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val appleIdService: AppleIdService,
    private val memberService: MemberService,
    private val authService: AuthService
) {
    @PostMapping("/oauth2/token")
    fun signInWithApple(@RequestBody request: SignInWithAppleRequest): TokenResponse {
        val (accountId, refreshToken) = appleIdService.verify(request.code)
        memberService.register(accountId, refreshToken)
        return authService.issueToken(accountId)
    }
}