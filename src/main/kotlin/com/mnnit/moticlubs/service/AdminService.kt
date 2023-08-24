package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.Admin
import com.mnnit.moticlubs.dao.Member
import com.mnnit.moticlubs.dto.request.AssignAdminDTO
import com.mnnit.moticlubs.repository.AdminRepository
import com.mnnit.moticlubs.repository.ChannelRepository
import com.mnnit.moticlubs.repository.MemberRepository
import com.mnnit.moticlubs.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AdminService(
    private val adminRepository: AdminRepository,
    private val channelRepository: ChannelRepository,
    private val memberRepository: MemberRepository,
    private val userRepository: UserRepository,
) {

    fun saveAdmin(dto: AssignAdminDTO): Mono<Admin> = userRepository.findByRegNo(dto.regNo)
        .flatMap { user -> adminRepository.save(Admin(dto.cid, user.uid)) }
        .flatMap { admin ->
            channelRepository.findByCid(admin.cid)
                .filter { it.private }
                .flatMap { channel -> memberRepository.save(Member(chid = channel.chid, uid = admin.uid)) }
                .then(Mono.just(admin))
        }

    fun removeAdmin(dto: AssignAdminDTO): Mono<Void> = userRepository.findByRegNo(dto.regNo)
        .flatMap { user ->
            memberRepository.deleteAllByUid(user.uid)
                .and(adminRepository.delete(Admin(dto.cid, user.uid)))
        }
}
