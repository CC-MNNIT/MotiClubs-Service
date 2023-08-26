package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.Member
import com.mnnit.moticlubs.dto.request.MembersDTO
import com.mnnit.moticlubs.repository.MemberRepository
import com.mnnit.moticlubs.utils.storeCache
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {

    @CachePut("chid_members", key = "#dto.chid")
    fun addMembers(dto: MembersDTO): Mono<List<Member>> = Flux.fromIterable(dto.users)
        .flatMap { uid -> memberRepository.save(Member(chid = dto.chid, uid = uid)) }
        .collectList()

    @CacheEvict("chid_members", key = "#dto.chid")
    fun removeMembers(dto: MembersDTO): Mono<Void> = Flux.fromIterable(dto.users)
        .flatMap { uid -> memberRepository.delete(Member(chid = dto.chid, uid = uid)) }
        .then()

    @Cacheable("chid_members", key = "#chid")
    fun getMembersByChid(chid: Long): Mono<List<Member>> = memberRepository
        .findAllByChid(chid)
        .collectList()
        .storeCache()
}
