package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.Member
import com.mnnit.moticlubs.dto.request.MembersDTO
import com.mnnit.moticlubs.repository.MemberRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {

    fun addMembers(dto: MembersDTO): Mono<List<Member>> = Flux.fromIterable(dto.users)
        .flatMap { uid -> memberRepository.save(Member(chid = dto.chid, uid = uid)) }
        .collectList()

    fun removeMembers(dto: MembersDTO): Mono<Void> = Flux.fromIterable(dto.users)
        .flatMap { uid -> memberRepository.delete(Member(chid = dto.chid, uid = uid)) }
        .then()

    fun getMembersByCid(cid: Long): Mono<List<Member>> = memberRepository
        .findAllByChid(cid)
        .collectList()
}
