package com.chillin.exception

import io.jsonwebtoken.JwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionController {

    @ExceptionHandler(JwtException::class)
    fun handleJwtException(e: JwtException): ResponseEntity<ExceptionResponse> {
        val response =
            ExceptionResponse(401, HttpStatus.UNAUTHORIZED.reasonPhrase, e.message ?: "Failed to verify token")
        return ResponseEntity.status(401).body(response)
    }
}