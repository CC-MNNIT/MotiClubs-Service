package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.Admin
import com.mnnit.moticlubs.repository.AdminRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AdminService(
    private val adminRepository: AdminRepository,
) {

    fun saveAdmin(admin: Admin): Mono<Admin> = adminRepository.save(admin)

    fun removeAdmin(admin: Admin): Mono<Void> = adminRepository.delete(admin)
}
