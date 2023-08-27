package com.mnnit.moticlubs.web.client

import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Service
class GoogleClient(
    private val googleWebClient: WebClient,
) {

    companion object {
        private const val TAG = "[FETCH_PUB]"
        private val LOGGER = LoggerFactory.getLogger(GoogleClient::class.java)
    }

    fun fetchPublicKeys(): Mono<Pair<Long, Map<String, String>>> = googleWebClient
        .get()
        .exchangeToMono { response ->
            val age = response.headers().header(HttpHeaders.CACHE_CONTROL)[0]
                .split(",")
                .map { it.trim() }
                .find { it.startsWith("max-age") }
                ?.let { it.split("=")[1].toLong() } ?: 0

            LOGGER.info("$TAG next age: $age")

            response
                .bodyToMono(object : ParameterizedTypeReference<Map<String, String>>() {})
                .map { hashMap ->
                    LOGGER.info("$TAG keys: ${hashMap.keys}")
                    Pair(age, hashMap)
                }
        }
        .onErrorMap { ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, it.localizedMessage) }
}
