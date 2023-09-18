package com.mnnit.moticlubs

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server

@SecurityScheme(
    type = SecuritySchemeType.HTTP,
    name = "Firebase Auth",
    scheme = "Bearer",
)
@OpenAPIDefinition(
    info = Info(
        title = "MotiClubs Service",
        description = "Backend to provide service to the MotiClubs App",
        contact = Contact(
            name = "CC-Club MNNIT",
            url = "https://github.com/CC-MNNIT",
            email = "computer.club@mnnit.ac.in",
        ),
    ),
    servers = [
        Server(
            url = "http://localhost:8002/",
            description = "Localhost",
        ),
        Server(
            url = "https://sac.mnnit.ac.in/moticlubs/",
            description = "Production",
        ),
    ],
    security = [SecurityRequirement(name = "Firebase Auth")],
)
class OpenAPIDefinition
