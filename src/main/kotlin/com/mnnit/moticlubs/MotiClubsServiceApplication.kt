package com.mnnit.moticlubs

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@SpringBootApplication(exclude = [ReactiveUserDetailsServiceAutoConfiguration::class])
@EnableR2dbcRepositories
@ConfigurationPropertiesScan
@EnableCaching
@EnableScheduling
class MotiClubsServiceApplication {

    @GetMapping("/logout")
    @Hidden
    fun logout(exchange: ServerWebExchange): Mono<Void> = exchange.session.flatMap { session -> session.invalidate() }
}

fun main(args: Array<String>) {
    runApplication<MotiClubsServiceApplication>(*args)
}
