package com.mnnit.moticlubs.utils

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UnauthorizedException(
    override val message: String,
) : ResponseStatusException(
    HttpStatus.UNAUTHORIZED,
    message
)
