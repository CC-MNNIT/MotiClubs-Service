package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.Channel
import com.mnnit.moticlubs.dto.request.UpdateChannelDTO
import com.mnnit.moticlubs.service.ChannelService
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.CHANNEL_ID_CLAIM
import com.mnnit.moticlubs.utils.Constants.CHANNEL_ROUTE
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/$BASE_PATH/$CHANNEL_ROUTE")
@Tag(name = "ChannelRoute")
class ChannelController(
    private val pathAuthorization: PathAuthorization,
    private val channelService: ChannelService,
) {

    companion object {
        private val LOGGER = ServiceLogger.getLogger(ChannelController::class.java)
    }

    @GetMapping
    @Operation(summary = "Returns list of all channels")
    fun getAllChannels(): Mono<List<Channel>> = pathAuthorization
        .userAuthorization()
        .flatMap { uid ->
            LOGGER.info("getAllChannels")
            channelService.getAllChannels(uid)
        }
        .wrapError()

    @GetMapping("/{$CHANNEL_ID_CLAIM}")
    @Operation(summary = "Returns single channel from channelId")
    fun getChannelFromChid(@PathVariable channelId: Long): Mono<Channel> = pathAuthorization
        .userAuthorization()
        .flatMap { uid ->
            LOGGER.info("getChannelFromChid: chid: $channelId")
            channelService.getChannelByChID(uid, channelId)
        }
        .wrapError()

    @PostMapping
    @Operation(summary = "Creates a channel in the club")
    fun createChannel(@RequestBody channel: Channel): Mono<Channel> = pathAuthorization
        .clubAuthorization(channel.cid)
        .flatMap {
            LOGGER.info("createChannel: channel: $channel")
            channelService.saveChannel(channel)
        }
        .wrapError()

    @PutMapping("/{$CHANNEL_ID_CLAIM}")
    @Operation(summary = "Updates the name of channel")
    fun updateChannel(
        @RequestBody dto: UpdateChannelDTO,
        @PathVariable channelId: Long
    ): Mono<Channel> = pathAuthorization
        .clubAuthorization(dto.cid)
        .flatMap {
            LOGGER.info("updateChannel: dto: $dto; chid: $channelId")
            channelService.updateChannel(channelId, dto)
        }
        .wrapError()

    @DeleteMapping("/{$CHANNEL_ID_CLAIM}")
    @Operation(summary = "Deletes the channel and the posts in it")
    fun deleteChannel(
        @RequestParam clubId: Long,
        @PathVariable channelId: Long
    ): Mono<Void> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap {
            LOGGER.info("deleteChannel: chid: $channelId; cid: $clubId")
            channelService.deleteChannelByChID(channelId)
        }
        .wrapError()
}
