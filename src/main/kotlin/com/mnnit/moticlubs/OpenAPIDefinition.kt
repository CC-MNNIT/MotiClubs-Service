package com.mnnit.moticlubs

import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class OpenAPIDefinition {

    @Bean
    fun openApiConfig(environment: Environment): OpenApiCustomizer = OpenApiCustomizer {
        it.info = Info().apply {
            title = "MotiClubs Service"
            description = "Backend to provide service to the MotiClubs App"
            contact = Contact().apply {
                name = "CC-Club MNNIT"
                url = "https://github.com/CC-MNNIT"
                email = "computer.club@mnnit.ac.in"
            }
        }

        it.servers = listOf(
            Server().apply {
                if (environment.activeProfiles.contains("prod")) {
                    url = "https://sac.mnnit.ac.in/moticlubs/"
                    description = "Production"
                } else {
                    url = "http://localhost:8002/"
                    description = "Localhost"
                }
            },
        )
    }
}
