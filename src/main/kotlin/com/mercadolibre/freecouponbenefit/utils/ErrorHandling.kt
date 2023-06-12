package com.mercadolibre.freecouponbenefit.utils

import com.mercadolibre.freecouponbenefit.exception.BadRequestException
import com.mercadolibre.freecouponbenefit.exception.NotFoundException
import javax.servlet.http.HttpServletRequest
import org.springframework.dao.DataAccessException
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

private const val STACKTRACE_LINES = 10

/**
 * Support for error handling in spring boot rest controllers.
 */
@ControllerAdvice
class ErrorControllerAdvice : ResponseEntityExceptionHandler() {

    override fun handleHttpRequestMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException,
        headers: HttpHeaders?,
        status: HttpStatus,
        request: WebRequest?
    ): ResponseEntity<Any>? {
        val errorMsg = ex.getMessage()

        logger.error("A HttpRequestMethodNotSupportedException occurred, details='$errorMsg'" + ex.truncatedStackTrace(STACKTRACE_LINES))

        return ResponseEntity(ApiError(status, errorMsg), status)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errorMsg = ex.getMessage()

        logger.error("A HttpMessageNotReadableException occurred, details='$errorMsg'" + ex.truncatedStackTrace(STACKTRACE_LINES))

        return ResponseEntity(ApiError(status, errorMsg), status)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(request: HttpServletRequest, ex: BadRequestException): ResponseEntity<ApiError> {
        val errorMsg = ex.getMessage()
        val status = HttpStatus.BAD_REQUEST

        logger.error("A BadRequestException occurred, details='$errorMsg'" + ex.truncatedStackTrace(STACKTRACE_LINES))

        return ResponseEntity(ApiError(status, errorMsg), status)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleUnsupportedOperationException(request: HttpServletRequest, ex: NotFoundException): ResponseEntity<ApiError> {
        val errorMsg = ex.getMessage()
        val status = HttpStatus.NOT_FOUND

        logger.error("A NotFoundException occurred, details='$errorMsg'" + ex.truncatedStackTrace(STACKTRACE_LINES))

        return ResponseEntity(ApiError(status, errorMsg), status)
    }

    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKeyException(request: HttpServletRequest, ex: DuplicateKeyException): ResponseEntity<ApiError> {
        val errorMsg = ex.getMessage()
        val status = HttpStatus.BAD_REQUEST

        logger.error("A DuplicateKeyException occurred, details='$errorMsg'" + ex.truncatedStackTrace(STACKTRACE_LINES))

        return ResponseEntity(ApiError(status, errorMsg), status)
    }

    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccesssException(request: HttpServletRequest, ex: DataAccessException): ResponseEntity<ApiError> {
        val errorMsg = ex.getMessage()
        val status = HttpStatus.INTERNAL_SERVER_ERROR

        logger.error("A DataAccessException occurred, details='$errorMsg'" + ex.truncatedStackTrace(STACKTRACE_LINES))

        return ResponseEntity(ApiError(status, errorMsg), status)
    }

    @ExceptionHandler(UnsupportedOperationException::class)
    fun handleUnsupportedOperationException(request: HttpServletRequest, ex: UnsupportedOperationException): ResponseEntity<ApiError> {
        val errorMsg = ex.getMessage()
        val status = HttpStatus.NOT_IMPLEMENTED

        logger.error("A UnsupportedOperationException occurred, details='$errorMsg'" + ex.truncatedStackTrace(STACKTRACE_LINES))

        return ResponseEntity(ApiError(status, errorMsg), status)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun argumentTypeMismatchException(request: HttpServletRequest, ex: MethodArgumentTypeMismatchException): ResponseEntity<ApiError> {
        val errorMsg = ex.getMessage()
        val status = HttpStatus.BAD_REQUEST

        logger.error("A MethodArgumentTypeMismatchException occurred, details='$errorMsg'" + ex.truncatedStackTrace(STACKTRACE_LINES))

        return ResponseEntity(ApiError(status, errorMsg), status)
    }

    @ExceptionHandler(Exception::class)
    fun handleAll(ex: Exception, request: WebRequest?): ResponseEntity<Any>? {
        val errorMsg = ex.getMessage()
        val status = HttpStatus.INTERNAL_SERVER_ERROR

        logger.error("A Exception occurred, details='$errorMsg'" + ex.truncatedStackTrace(STACKTRACE_LINES))

        return ResponseEntity(ApiError(status, errorMsg), status)
    }
}

data class ApiError(
    val statusCode: Int,
    val reason: String,
    val messages: List<String>
) {

    constructor(httpStatus: HttpStatus, message: String?) : this(
        statusCode = httpStatus.value(),
        reason = httpStatus.reasonPhrase,
        messages = listOf(message ?: "Unexpected Error")
    )

    constructor(httpStatus: HttpStatus, messages: List<String>) : this(
        statusCode = httpStatus.value(),
        reason = httpStatus.reasonPhrase,
        messages = messages
    )
}
