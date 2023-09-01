package com.mnnit.moticlubs.utils

import com.mnnit.moticlubs.utils.ResponseStamp.getString
import com.mnnit.moticlubs.utils.ResponseStamp.invalidateStamp
import com.mnnit.moticlubs.utils.ResponseStamp.validateLast
import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.Duration

fun <T> Mono<T>.wrapError(): Mono<T> = onErrorMap {
    when (it) {
        is UnauthorizedException,
        is CachedException -> it

        else -> ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            it.localizedMessage
        )
    }
}

fun <T : Any> apiWrapper(
    key: ResponseStamp.StampKey,
    stampValue: Long,
    authorization: () -> Mono<Long>,
    serviceCall: (data: Long) -> Mono<T>
): Mono<ResponseEntity<T>> = authorization()
    .flatMap { if (key.validateLast(stampValue)) Mono.empty() else Mono.just(it) }
    .flatMap { serviceCall(it) }
    .map {
        ResponseEntity.ok()
            .header(Constants.STAMP_HEADER, key.getString(stampValue))
            .body(it)
    }
    .switchIfEmpty {
        Mono.just(
            ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                .header(Constants.STAMP_HEADER, key.getString(stampValue))
                .build()
        )
    }
    .wrapError()

fun <T> Mono<T>.invalidateStamp(
    getStampKey: (t: T) -> ResponseStamp.StampKey,
): Mono<ResponseEntity<T>> = map {
    val updatedStamp = getStampKey(it).invalidateStamp()
    ResponseEntity.ok()
        .header(Constants.STAMP_HEADER, updatedStamp.toString())
        .body(it)
}

fun Mono<Void>.invalidateStamp(
    getStampKey: () -> ResponseStamp.StampKey,
): Mono<ResponseEntity<Void>> = then(Mono.fromCallable {
    val updatedStamp = getStampKey().invalidateStamp()
    ResponseEntity.ok()
        .header(Constants.STAMP_HEADER, updatedStamp.toString())
        .build()
})

fun <T> Mono<T>.storeCache(): Mono<T> = cache(Duration.ofSeconds(120))

fun getReqId(): String = try {
    MDC.get("req")
} catch (_: Exception) {
    "null"
}

fun putReqId(reqId: String) = MDC.put("req", reqId)
