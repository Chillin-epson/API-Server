package com.chillin.security

import com.chillin.auth.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader(HttpHeaders.AUTHORIZATION)
            ?.substringAfter(BEARER)
            ?.trim()

        if (token != null) {
            val accountId = jwtProvider.validate(token).subject
            val authentication = UsernamePasswordAuthenticationToken(accountId, null, emptyList())

            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }

    companion object {
        private const val BEARER = "Bearer"
    }
}
