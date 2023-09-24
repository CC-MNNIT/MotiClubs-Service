package com.mnnit.moticlubs.web.security

import com.mnnit.moticlubs.dao.Admin
import com.mnnit.moticlubs.repository.AdminRepository
import com.mnnit.moticlubs.repository.SuperAdminRepository
import com.mnnit.moticlubs.repository.UserRepository
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.UnauthorizedException
import com.mnnit.moticlubs.utils.getReqId
import com.mnnit.moticlubs.utils.putReqId
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class PathAuthorization(
    private val adminRepository: AdminRepository,
    private val superAdminRepository: SuperAdminRepository,
    private val userRepository: UserRepository,
) {

    companion object {
        private val LOGGER = ServiceLogger.getLogger(PathAuthorization::class.java)
    }

    fun userAuthorization(): Mono<Long> = ReactiveSecurityContextHolder.getContext()
        .map { it.authentication.principal }
        .flatMap {
            when (it) {
                is DefaultOidcUser -> validateOidcUser(it)
                is AuthenticationToken -> Mono.just(Pair(it.userId, it.isEmailVerified))
                else -> Mono.error(UnauthorizedException("Invalid authorization means"))
            }
        }
        .flatMap { (userId, isEmailVerified) ->
            userId ?: return@flatMap Mono.error(UnauthorizedException("Missing user ID claim"))

            if (!isEmailVerified) {
                return@flatMap Mono.error(UnauthorizedException("Please verify email ID"))
            }

            LOGGER.info("userAuthorization: success [$userId]")
            Mono.just(userId)
        }

    private fun validateOidcUser(
        user: DefaultOidcUser,
    ): Mono<Pair<Long, Boolean>> = userRepository.findByEmail(user.email)
        .map { Pair(it.uid, user.emailVerified) }
        .switchIfEmpty { Mono.error(UnauthorizedException("Invalid user")) }

    fun clubAuthorization(cid: Long): Mono<Long> = userAuthorization()
        .flatMap { uid ->
            val admin = Admin(cid, uid)
            val reqId = getReqId()
            adminRepository.exists(Admin(cid, uid))
                .flatMap {
                    putReqId(reqId)
                    if (it) {
                        LOGGER.info("clubAuthorization: success [$admin]")
                        Mono.just(uid)
                    } else {
                        LOGGER.warn("clubAuthorization: unauthorized: [$admin]")
                        Mono.error(UnauthorizedException("User is not club admin"))
                    }
                }
        }

    fun superAdminAuthorization(): Mono<Long> = userAuthorization()
        .flatMap { uid ->
            val reqId = getReqId()
            superAdminRepository.existsById(uid)
                .flatMap {
                    putReqId(reqId)
                    if (it) {
                        LOGGER.info("superAdminAuthorization: success [$uid]")
                        Mono.just(uid)
                    } else {
                        LOGGER.warn("superAdminAuthorization: unauthorized: [$uid]")
                        Mono.error(UnauthorizedException("User is not super admin"))
                    }
                }
        }
}
