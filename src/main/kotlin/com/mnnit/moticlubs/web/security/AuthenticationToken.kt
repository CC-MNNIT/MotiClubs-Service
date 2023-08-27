package com.mnnit.moticlubs.web.security

import com.auth0.jwt.interfaces.DecodedJWT

class AuthenticationToken(
    private val decodedJWT: DecodedJWT
) {

    val isEmailVerified
        get(): Boolean = decodedJWT.getClaim("email_verified")?.asBoolean() ?: false

    val userId
        get(): Long? = decodedJWT.getClaim("userId")?.asLong()
}
