package tech.espero.vendingmachine.exception

import com.fasterxml.jackson.core.JsonProcessingException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class ExceptionHandling {

    @ExceptionHandler(ProductAmountTooLowException::class)
    @ResponseBody
    fun handleException(e: ProductAmountTooLowException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.BAD_REQUEST, request)
        return toResponse(HttpStatus.BAD_REQUEST, map)
    }

    @ExceptionHandler(ProductCouldNotBeDeletedException::class)
    @ResponseBody
    fun handleException(e: ProductCouldNotBeDeletedException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.NOT_FOUND, request)
        return toResponse(HttpStatus.NOT_FOUND, map)
    }

    @ExceptionHandler(IncorrectSellerException::class)
    @ResponseBody
    fun handleException(e: IncorrectSellerException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.BAD_REQUEST, request)
        return toResponse(HttpStatus.BAD_REQUEST, map)
    }

    @ExceptionHandler(ProductCostMustBeDivisibleByFiveException::class)
    @ResponseBody
    fun handleException(e: ProductCostMustBeDivisibleByFiveException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.BAD_REQUEST, request)
        return toResponse(HttpStatus.BAD_REQUEST, map)
    }

    @ExceptionHandler(ProductCostCanNotBeNegativeException::class)
    @ResponseBody
    fun handleException(e: ProductCostCanNotBeNegativeException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.BAD_REQUEST, request)
        return toResponse(HttpStatus.BAD_REQUEST, map)
    }

    @ExceptionHandler(ProductAmountCanNotBeNegativeException::class)
    @ResponseBody
    fun handleException(e: ProductAmountCanNotBeNegativeException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.BAD_REQUEST, request)
        return toResponse(HttpStatus.BAD_REQUEST, map)
    }

    @ExceptionHandler(ProductAlreadyExistsException::class)
    @ResponseBody
    fun handleException(e: ProductAlreadyExistsException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.BAD_REQUEST, request)
        return toResponse(HttpStatus.BAD_REQUEST, map)
    }

    @ExceptionHandler(ChangeNotPossibleException::class)
    @ResponseBody
    fun handleException(e: ChangeNotPossibleException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.BAD_REQUEST, request)
        return toResponse(HttpStatus.BAD_REQUEST, map)
    }

    @ExceptionHandler(DepositTooLowException::class)
    @ResponseBody
    fun handleException(e: DepositTooLowException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.BAD_REQUEST, request)
        return toResponse(HttpStatus.BAD_REQUEST, map)
    }

    @ExceptionHandler(ProductNotAvailableException::class)
    @ResponseBody
    fun handleException(e: ProductNotAvailableException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.BAD_REQUEST, request)
        return toResponse(HttpStatus.BAD_REQUEST, map)
    }

    @ExceptionHandler(ProductNotFoundException::class)
    @ResponseBody
    fun handleException(e: ProductNotFoundException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.NOT_FOUND, request)
        return toResponse(HttpStatus.NOT_FOUND, map)
    }

    @ExceptionHandler(IncorrectAmountException::class)
    @ResponseBody
    fun handleException(e: IncorrectAmountException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.BAD_REQUEST, request)
        return toResponse(HttpStatus.BAD_REQUEST, map)
    }

    @ExceptionHandler(UserRoleNotAuthorizedException::class)
    @ResponseBody
    fun handleException(e: UserRoleNotAuthorizedException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.UNAUTHORIZED, request)
        return toResponse(HttpStatus.UNAUTHORIZED, map)
    }

    @ExceptionHandler(UserCouldNotBeDeletedException::class)
    @ResponseBody
    fun handleException(e: UserCouldNotBeDeletedException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.METHOD_NOT_ALLOWED, request)
        return toResponse(HttpStatus.METHOD_NOT_ALLOWED, map)
    }

    @ExceptionHandler(DepositNegativeException::class)
    @ResponseBody
    fun handleException(e: DepositNegativeException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.BAD_REQUEST, request)
        return toResponse(HttpStatus.BAD_REQUEST, map)
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    @ResponseBody
    fun handleException(e: UserAlreadyExistsException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.CONFLICT, request)
        return toResponse(HttpStatus.CONFLICT, map)
    }

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseBody
    fun handleException(e: UserNotFoundException, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {
        val map = createResponse(e, HttpStatus.NOT_FOUND, request)
        return toResponse(HttpStatus.NOT_FOUND, map)
    }

    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun handleException(
        e: Exception,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<*> {
        val defaultHandlerExceptionResolver = DefaultHandlerExceptionResolver()
        defaultHandlerExceptionResolver.resolveException(request, response, this, e)

        val map = createResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request)
        return toResponse(HttpStatus.INTERNAL_SERVER_ERROR, map)
    }

    private fun createResponse(
        e: java.lang.Exception,
        httpStatus: HttpStatus,
        request: HttpServletRequest
    ): Map<String, Any> {
        // Standard
        val response: MutableMap<String, Any> = LinkedHashMap()
        response["timestamp"] = Date()
        response["status"] = httpStatus.value()
        response["error"] = httpStatus.reasonPhrase
        response["exception"] = e.javaClass.simpleName
        response["message"] = e.message ?: ""
        response["path"] = request.requestURI
        return response
    }

    private fun toResponse(statusCode: HttpStatus, map: Map<String, Any>): ResponseEntity<*> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        return ResponseEntity(map, headers, statusCode)
    }
}