package com.chillin.auth

import io.jsonwebtoken.Claims
import okhttp3.FormBody
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.security.PublicKey

@Component
@EnableConfigurationProperties(AppleIdProperties::class)
class AppleIdProvider(
    private val props: AppleIdProperties
) {
    fun tokenForm(code: String): FormBody = props.toTokenFormBody(code)
    fun verifyToken(idToken: String, publicKey: PublicKey): Claims = props.verifyToken(idToken, publicKey)
}