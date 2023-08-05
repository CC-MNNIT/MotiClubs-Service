package com.mnnit.moticlubs.utils

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

fun <T> Mono<T>.wrapError(): Mono<T> = onErrorMap {
    ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, it.localizedMessage)
}
