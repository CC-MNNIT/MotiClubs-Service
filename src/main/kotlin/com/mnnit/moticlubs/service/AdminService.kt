package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.Admin
import com.mnnit.moticlubs.dao.Member
import com.mnnit.moticlubs.dto.request.AssignAdminDTO
import com.mnnit.moticlubs.repository.AdminRepository
import com.mnnit.moticlubs.repository.ChannelRepository
import com.mnnit.moticlubs.repository.MemberRepository
import com.mnnit.moticlubs.repository.UserRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AdminService(
    private val adminRepository: AdminRepository,
    private val channelRepository: ChannelRepository,
    private val memberRepository: MemberRepository,
    private val userRepository: UserRepository,
) {

    @CacheEvict("admins", allEntries = true)
    fun saveAdmin(dto: AssignAdminDTO): Mono<Admin> = userRepository.findByRegNo(dto.regNo)
        .flatMap { user -> adminRepository.save(Admin(dto.cid, user.uid)) }
        .flatMap { admin ->
            channelRepository.findByCid(admin.cid)
                .filter { it.private }
                .flatMap { channel -> memberRepository.save(Member(chid = channel.chid, uid = admin.uid)) }
                .then(Mono.just(admin))
        }

    @CacheEvict("admins", allEntries = true)
    fun removeAdmin(dto: AssignAdminDTO): Mono<Void> = userRepository.findByRegNo(dto.regNo)
        .flatMap { user ->
            channelRepository.findByCid(dto.cid)
                .filter { it.private }
                .flatMap { channel -> memberRepository.delete(Member(channel.chid, user.uid)) }
                .then(adminRepository.delete(Admin(dto.cid, user.uid)))
        }
}
