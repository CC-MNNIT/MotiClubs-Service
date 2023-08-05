package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.ChannelRepository
import com.mnnit.moticlubs.dao.PostRepository
import com.mnnit.moticlubs.dto.Channel
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ChannelService(
    private val channelRepository: ChannelRepository,
    private val postRepository: PostRepository,
) {

    fun saveChannel(channel: Channel): Mono<Channel> = channelRepository.save(channel)

    fun getAllChannels(): Mono<List<Channel>> = channelRepository
        .findAll()
        .collectList()

    fun getChannelsByCID(cid: Long): Mono<List<Channel>> = channelRepository
        .findChannelsByCid(cid)
        .collectList()

    fun getChannelByChID(chid: Long): Mono<Channel> = channelRepository.findById(chid)

    fun updateChannelName(chid: Long, name: String): Mono<Channel> = channelRepository
        .findById(chid)
        .flatMap { saveChannel(it.copy(name = name)) }

    fun deleteChannelByChID(chid: Long): Mono<Void> = channelRepository.deleteById(chid)
        .then(postRepository.deleteAllByChid(chid))
}
