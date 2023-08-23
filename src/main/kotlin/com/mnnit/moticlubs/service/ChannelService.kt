package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.Channel
import com.mnnit.moticlubs.repository.ChannelRepository
import com.mnnit.moticlubs.repository.PostRepository
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

    fun getChannelByChID(chid: Long): Mono<Channel> = channelRepository.findById(chid)

    fun updateChannelName(chid: Long, name: String): Mono<Channel> = channelRepository.updateName(chid, name)

    fun deleteChannelByChID(chid: Long): Mono<Void> = channelRepository.deleteById(chid)
}
