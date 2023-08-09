package com.mnnit.moticlubs.utils

import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

fun <T> Mono<T>.wrapError(): Mono<T> = onErrorMap {
    if (it is ResponseStatusException) it else ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        it.localizedMessage
    )
}

fun getReqId(): String = MDC.get("req")
fun putReqId(reqId: String) = MDC.put("req", reqId)
