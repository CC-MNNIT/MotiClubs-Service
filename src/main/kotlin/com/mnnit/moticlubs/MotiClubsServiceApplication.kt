package com.mnnit.moticlubs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication(exclude = [ReactiveUserDetailsServiceAutoConfiguration::class])
@EnableR2dbcRepositories
class MotiClubsServiceApplication

fun main(args: Array<String>) {
    runApplication<MotiClubsServiceApplication>(*args)
}
