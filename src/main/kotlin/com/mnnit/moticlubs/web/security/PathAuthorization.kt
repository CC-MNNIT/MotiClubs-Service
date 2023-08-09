package com.mnnit.moticlubs.web.security

import com.google.firebase.auth.FirebaseToken
import com.mnnit.moticlubs.dao.Admin
import com.mnnit.moticlubs.repository.AdminRepository
import com.mnnit.moticlubs.repository.SuperAdminRepository
import com.mnnit.moticlubs.utils.Constants.USER_ID_CLAIM
import com.mnnit.moticlubs.utils.ServiceLogger
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

    companion object {
        private val LOGGER = ServiceLogger.getLogger(PathAuthorization::class.java)
    }

    fun userAuthorization(): Mono<Long> = ReactiveSecurityContextHolder.getContext()
        .flatMap { ctx ->
            val token = ctx.authentication.principal as FirebaseToken
            val userId = token.claims[USER_ID_CLAIM]?.toString()?.toLong()
                ?: return@flatMap Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing user ID claim"))

            LOGGER.info("userAuthorization: success [$userId] ")
            Mono.just(userId)
        }

    fun clubAuthorization(cid: Long): Mono<Long> = userAuthorization()
        .flatMap { uid ->
            val admin = Admin(cid, uid)
            adminRepository.exists(Admin(cid, uid))
                .flatMap {
                    if (it) {
                        LOGGER.info("clubAuthorization: success [$admin] ")
                        Mono.just(uid)
                    } else {
                        LOGGER.warn("clubAuthorization: unauthorized: [$admin]")
                        Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not club admin"))
                    }
                }
        }

    fun superAdminAuthorization(): Mono<Long> = userAuthorization()
        .flatMap { uid ->
            superAdminRepository.existsById(uid)
                .flatMap {
                    if (it) {
                        LOGGER.info("superAdminAuthorization: success [$uid] ")
                        Mono.just(uid)
                    } else {
                        LOGGER.warn("superAdminAuthorization: unauthorized: [$uid] ")
                        Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not super admin"))
                    }
                }
        }
}
