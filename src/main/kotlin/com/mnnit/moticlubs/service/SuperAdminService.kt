package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.SuperAdminRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class SuperAdminService(
    private val superAdminRepository: SuperAdminRepository,
) {

    fun isSuperAdmin(uid: Long): Mono<Boolean> = superAdminRepository.existsById(uid)
}
