package com.mnnit.moticlubs.web.security

import com.google.firebase.auth.FirebaseToken
import com.mnnit.moticlubs.dao.AdminRepository
import com.mnnit.moticlubs.dao.SuperAdminRepository
import com.mnnit.moticlubs.dto.Admin
import com.mnnit.moticlubs.utils.Constants.USER_ID_CLAIM
import org.springframework.data.domain.Example
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Component
class PathAuthorization(
    private val adminRepository: AdminRepository,
    private val superAdminRepository: SuperAdminRepository,
) {

    fun userAuthorization(): Mono<Long> = ReactiveSecurityContextHolder.getContext()
        .flatMap { ctx ->
            val token = ctx.authentication.principal as FirebaseToken
            val userId = token.claims[USER_ID_CLAIM]?.toString()?.toLong()
                ?: return@flatMap Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing user ID claim"))
            Mono.just(userId)
        }

    fun clubAuthorization(cid: Long): Mono<Long> = userAuthorization()
        .flatMap { uid ->
            adminRepository.exists(Example.of(Admin(cid, uid)))
                .flatMap {
                    if (it) {
                        Mono.just(uid)
                    } else {
                        Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not club admin"))
                    }
                }
        }

    fun superAdminAuthorization(): Mono<Long> = userAuthorization()
        .flatMap { uid ->
            superAdminRepository.existsById(uid)
                .flatMap {
                    if (it) {
                        Mono.just(uid)
                    } else {
                        Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not super admin"))
                    }
                }
        }
}
