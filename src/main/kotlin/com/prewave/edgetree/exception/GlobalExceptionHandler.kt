package com.prewave.edgetree.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(
        EdgeAlreadyExistsException::class,
        EdgeNotFoundException::class,
        EdgeNodeNotFoundException::class,
        EdgeCycleDetectedException::class,
        EdgeCycleUndecidableException::class,
        EdgeNodeAlreadyHasParentException::class,
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

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ApiError> {
        val errorMessage = ex.bindingResult.fieldErrors.map { "${it.defaultMessage}" }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError("Validation failed: $errorMessage"))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError("Unexpected error: ${ex.message}"))
}