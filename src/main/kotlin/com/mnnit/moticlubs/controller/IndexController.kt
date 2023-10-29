package com.mnnit.moticlubs.controller

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/")
@Hidden
class IndexController {

    @GetMapping("", produces = ["text/html"])
    fun index(
        @Value("classpath:templates/index.html") html: Resource,
    ): Mono<ResponseEntity<Resource>> = Mono.just(ResponseEntity.status(HttpStatus.OK).body(html))

    @GetMapping("/privacy", produces = ["text/html"])
    fun privacy(
        @Value("classpath:templates/privacy.html") html: Resource,
    ): Mono<ResponseEntity<Resource>> = Mono.just(ResponseEntity.status(HttpStatus.OK).body(html))
}
