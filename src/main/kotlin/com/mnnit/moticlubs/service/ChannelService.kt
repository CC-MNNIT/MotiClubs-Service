package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.Channel
import com.mnnit.moticlubs.dao.Member
import com.mnnit.moticlubs.dto.request.UpdateChannelDTO
import com.mnnit.moticlubs.repository.AdminRepository
import com.mnnit.moticlubs.repository.ChannelRepository
import com.mnnit.moticlubs.repository.MemberRepository
import com.mnnit.moticlubs.utils.UnauthorizedException
import com.mnnit.moticlubs.utils.storeCache
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ChannelService(
    private val adminRepository: AdminRepository,
    private val memberRepository: MemberRepository,
    private val channelRepository: ChannelRepository,
) {

    @CacheEvict(cacheNames = ["all_channels", "members"], allEntries = true)
    fun saveChannel(channel: Channel): Mono<Channel> = channelRepository.save(channel)
        .flatMap { it.updateChannelAccess(it.private, it.cid, it.chid) }

    @Cacheable("all_channels", key = "#uid")
    fun getAllChannels(uid: Long): Mono<List<Channel>> = channelRepository
        .findAll(uid)
        .collectList()
        .storeCache()

    fun getChannelByChID(uid: Long, chid: Long): Mono<Channel> = channelRepository.findById(chid)
        .flatMap { channel ->
            if (channel.private) {
                memberRepository.exists(Member(chid, uid))
                    .flatMap { exists ->
                        if (exists) {
                            Mono.just(channel)
                        } else {
                            Mono.error(UnauthorizedException("User is not allowed to view this channel"))
                        }
                    }
            } else Mono.just(channel)
        }

    @CacheEvict(cacheNames = ["all_channels", "members"], allEntries = true)
    fun updateChannel(chid: Long, dto: UpdateChannelDTO): Mono<Channel> = channelRepository.update(chid, dto)
        .flatMap { it.updateChannelAccess(dto.private, dto.cid, chid) }

    @CacheEvict(cacheNames = ["all_channels", "members", "post", "replies"], allEntries = true)
    fun deleteChannelByChID(chid: Long): Mono<Void> = channelRepository.deleteById(chid)

    private fun Channel.updateChannelAccess(private: Boolean, cid: Long, chid: Long): Mono<Channel> {
        return if (private) {
            adminRepository.findAllByCid(cid)
                .flatMap { admin -> memberRepository.save(Member(chid = chid, uid = admin.uid)) }
                .then(Mono.just(this))
        } else {
            memberRepository.deleteAllByChid(chid)
                .then(Mono.just(this))
        }
    }
}
