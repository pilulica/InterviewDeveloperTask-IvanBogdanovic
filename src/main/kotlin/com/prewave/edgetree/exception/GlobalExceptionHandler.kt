package com.prewave.edgetree.exception

import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Profile("!swagger")
class GlobalExceptionHandler {

    @ExceptionHandler(
        EdgeAlreadyExistsException::class,
        EdgeNotFoundException::class,
        EdgeNodeNotFoundException::class,
        EdgeCycleDetectedException::class,
        EdgeCycleUndecidableException::class
    )
    fun handleKnownExceptions(ex: RuntimeException): ResponseEntity<ApiError> {
        val status = when (ex) {
            is EdgeAlreadyExistsException -> HttpStatus.CONFLICT
            is EdgeNotFoundException -> HttpStatus.NOT_FOUND
            is EdgeNodeNotFoundException -> HttpStatus.NOT_FOUND
            is EdgeCycleDetectedException -> HttpStatus.BAD_REQUEST
            is EdgeCycleUndecidableException -> HttpStatus.BAD_REQUEST
            is EdgeNodeAlreadyHasParentException -> HttpStatus.BAD_REQUEST
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity.status(status).body(ApiError(ex.message ?: "Unexpected error"))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError("Unexpected error: ${ex.message}"))
}