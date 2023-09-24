package com.mnnit.moticlubs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(exclude = [ReactiveUserDetailsServiceAutoConfiguration::class])
@EnableR2dbcRepositories
@ConfigurationPropertiesScan
@EnableCaching
@EnableScheduling
class MotiClubsServiceApplication

fun main(args: Array<String>) {
    runApplication<MotiClubsServiceApplication>(*args)
}
