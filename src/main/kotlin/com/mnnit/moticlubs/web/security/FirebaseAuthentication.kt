package com.mnnit.moticlubs.web.security

import com.google.firebase.auth.FirebaseToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class FirebaseAuthentication(
    private val token: FirebaseToken,
    private var authenticated: Boolean = false,
) : Authentication {

    override fun getName(): String = "FirebaseTokenAuthorization"

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun getCredentials(): Any = token

    override fun getDetails(): Any = token

    override fun getPrincipal(): FirebaseToken = token

    override fun isAuthenticated(): Boolean = authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }
}
