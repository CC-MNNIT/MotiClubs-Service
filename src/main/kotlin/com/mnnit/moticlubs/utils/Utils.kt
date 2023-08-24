package com.mnnit.moticlubs.utils

import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

fun <T> Mono<T>.wrapError(): Mono<T> = onErrorMap {
    if (it is UnauthorizedException) it else ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        it.localizedMessage
    )
}

fun getReqId(): String = try {
    MDC.get("req")
} catch (_: Exception) {
    "null"
}

fun putReqId(reqId: String) = MDC.put("req", reqId)
