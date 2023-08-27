package com.mnnit.moticlubs.web.client

import com.mnnit.moticlubs.web.config.FirebaseConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@Lazy
@EnableConfigurationProperties(value = [FirebaseConfiguration.Properties::class])
class WebClientFactory {

    @Bean
    fun googleWebClient(properties: FirebaseConfiguration.Properties): WebClient = WebClient.builder()
        .baseUrl(properties.publicKeyUrl)
        .build()
}
