package com.mnnit.moticlubs

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme

@SecurityScheme(
    type = SecuritySchemeType.HTTP,
    name = "Firebase Auth",
    scheme = "Bearer"
)
@OpenAPIDefinition(
    info = Info(
        title = "MotiClubs Backend",
        description = "Backend to provide service of the app",
    ),
    security = [SecurityRequirement(name = "Firebase Auth")]
)
class OpenAPIDefinition
