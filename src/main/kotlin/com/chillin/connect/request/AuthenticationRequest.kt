package com.chillin.connect.request

import okhttp3.FormBody

data class AuthenticationRequest(
    private val username: String
) {
    private val grantType = "password"
    private val password = ""

    fun toFormBody(): FormBody {
        return FormBody.Builder()
            .add("grant_type", grantType)
            .add("username", username)
            .add("password", password)
            .build()
    }
}