package com.mnnit.moticlubs.utils

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UnauthorizedException(
    override val message: String,
) : ResponseStatusException(
    HttpStatus.UNAUTHORIZED,
    message,
)

class CachedException(
    override val message: String,
) : ResponseStatusException(
    HttpStatus.NOT_MODIFIED,
    message,
)

class BadRequestException(
    override val message: String,
) : ResponseStatusException(
    HttpStatus.BAD_REQUEST,
    message,
)

class NotFoundException(
    override val message: String,
) : ResponseStatusException(
    HttpStatus.NOT_FOUND,
    message,
)
