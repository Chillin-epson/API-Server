package com.chillin.auth

import com.chillin.auth.appleid.AppleIdService
import com.chillin.auth.request.SignInWithAppleRequest
import com.chillin.auth.response.TokenResponse
import com.chillin.member.MemberService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
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

    @PostMapping("/oauth2/refresh")
    fun refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) bearerToken: String): ResponseEntity<TokenResponse> {
        val token = bearerToken.substringAfter("Bearer").trim()
        val newToken = authService.reissueToken(token)

        return if (newToken != null) ResponseEntity.ok(newToken)
        else ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}