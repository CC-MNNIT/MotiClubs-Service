package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.AdminRepository
import com.mnnit.moticlubs.dto.Admin
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AdminService(
    private val adminRepository: AdminRepository,
) {

    fun saveAdmin(admin: Admin): Mono<Admin> = adminRepository.save(admin)

    fun getAdminsFromCID(cid: Long): Mono<List<Admin>> = adminRepository
        .findAll()
        .filter { it.cid == cid }
        .collectList()

    fun getAdminsFromUID(uid: Long): Mono<List<Admin>> = adminRepository
        .findAll()
        .filter { it.uid == uid }
        .collectList()

    fun removeAdmin(admin: Admin): Mono<Void> = adminRepository.delete(admin)
}
