package com.odeng.finance.common.infastructure

import com.odeng.finance.auth.application.AuthException
import com.odeng.finance.common.ValidationException
import com.odeng.finance.interfaces.rest.api.model.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

/**
 * Global exception handler for REST controllers.
 * Catches exceptions and converts them to standardized ErrorResponse objects.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    private companion object {
        val logger = KotlinLogging.logger {}
    }

    /**
     * Handles AuthException thrown during authentication.
     * Returns 401 Unauthorized.
     */
    @ExceptionHandler(AuthException::class)
    fun handleAuthException(
        ex: AuthException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error { "Authentication failed: ${ex.message}" }

        val errorResponse = ErrorResponse(
            timestamp = Instant.now(),
            status = ex.httpStatus.value(),
            error = "Unauthorized",
            messages = listOf(ex.message ?: "Authentication failed")
        )

        return ResponseEntity
            .status(ex.httpStatus)
            .body(errorResponse)
    }

    /**
     * Handles ValidationException thrown by domain validators.
     * Returns 400 Bad Request with validation error details.
     */
    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(
        ex: ValidationException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error { "Validation failed: ${ex.errors}" }

        val errorResponse = ErrorResponse(
            timestamp = Instant.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Failed",
            messages = ex.errors
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse)
    }

    /**
     * Handles Spring's Bean Validation exceptions (from @Valid annotations).
     * Returns 400 Bad Request with field validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors
            .map { "${it.field}: ${it.defaultMessage}" }

        logger.error { "Bean validation failed: $errors" }

        val errorResponse = ErrorResponse(
            timestamp = Instant.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Failed",
            messages = errors
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse)
    }

    /**
     * Handles IllegalArgumentException (from require() statements).
     * Returns 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        ex: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error { "Illegal argument: ${ex.message}" }

        val errorResponse = ErrorResponse(
            timestamp = Instant.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            messages = listOf(ex.message ?: "Invalid argument")
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse)
    }

    /**
     * Handles IllegalStateException (from check() statements).
     * Returns 409 Conflict.
     */
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(
        ex: IllegalStateException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error { "Illegal state: ${ex.message}" }

        val errorResponse = ErrorResponse(
            timestamp = Instant.now(),
            status = HttpStatus.CONFLICT.value(),
            error = "Conflict",
            messages = listOf(ex.message ?: "Invalid state")
        )

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(errorResponse)
    }

    /**
     * Handles AuthorizationDeniedException thrown by Spring Security @PreAuthorize.
     * Returns 403 Forbidden.
     */
    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleAuthorizationDeniedException(
        ex: AuthorizationDeniedException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error { "Authorization denied: ${ex.message}" }

        val errorResponse = ErrorResponse(
            timestamp = Instant.now(),
            status = HttpStatus.FORBIDDEN.value(),
            error = "Forbidden",
            messages = listOf("Access denied")
        )

        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(errorResponse)
    }

    /**
     * Handles all other unhandled exceptions.
     * Returns 500 Internal Server Error.
     */
    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error(ex) { "Unhandled exception: ${ex.message}" }

        val errorResponse = ErrorResponse(
            timestamp = Instant.now(),
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            messages = listOf("An unexpected error occurred")
        )

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse)
    }
}

