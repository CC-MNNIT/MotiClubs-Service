package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.Channel
import com.mnnit.moticlubs.dao.Member
import com.mnnit.moticlubs.dto.request.MembersDTO
import com.mnnit.moticlubs.dto.request.UpdateChannelDTO
import com.mnnit.moticlubs.service.ChannelService
import com.mnnit.moticlubs.service.MemberService
import com.mnnit.moticlubs.utils.*
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.CHANNEL_ID_CLAIM
import com.mnnit.moticlubs.utils.Constants.CHANNEL_ROUTE
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/$BASE_PATH/$CHANNEL_ROUTE")
@Tag(name = "ChannelRoute")
class ChannelController(
    private val pathAuthorization: PathAuthorization,
    private val channelService: ChannelService,
    private val memberService: MemberService,
) {

    companion object {
        private val LOGGER = ServiceLogger.getLogger(ChannelController::class.java)
    }

    @GetMapping
    @Operation(summary = "Returns list of all channels")
    fun getAllChannels(
        @RequestHeader(Constants.STAMP_HEADER) stamp: Long,
    ): Mono<ResponseEntity<List<Channel>>> = apiWrapper(
        key = ResponseStamp.CHANNEL,
        stampValue = stamp,
        authorization = pathAuthorization::userAuthorization,
        serviceCall = { uid ->
            LOGGER.info("getAllChannels")
            channelService.getAllChannels(uid)
        }
    )

    @GetMapping("/{$CHANNEL_ID_CLAIM}")
    @Operation(summary = "Returns single channel from channelId")
    fun getChannelFromChid(
        @PathVariable channelId: Long,
        @RequestHeader(Constants.STAMP_HEADER) stamp: Long,
    ): Mono<ResponseEntity<Channel>> = apiWrapper(
        key = ResponseStamp.CHANNEL,
        stampValue = stamp,
        authorization = pathAuthorization::userAuthorization,
        serviceCall = { uid ->
            LOGGER.info("getChannelFromChid: chid: $channelId")
            channelService.getChannelByChID(uid, channelId)
        }
    )

    @GetMapping("/members/{${CHANNEL_ID_CLAIM}}")
    @Operation(summary = "Returns list of userIds that are member of the channelId")
    fun getMembers(
        @PathVariable channelId: Long,
        @RequestHeader(Constants.STAMP_HEADER) stamp: Long,
    ): Mono<ResponseEntity<List<Member>>> = apiWrapper(
        key = ResponseStamp.MEMBER.withKey("$channelId"),
        stampValue = stamp,
        authorization = pathAuthorization::userAuthorization,
        serviceCall = {
            LOGGER.info("getMembers: chid: $channelId")
            memberService.getMembersByChid(channelId)
        }
    )

    @PostMapping("/members")
    @Operation(summary = "Makes list of userIds member of the clubId")
    fun addMembers(@RequestBody dto: MembersDTO): Mono<ResponseEntity<List<Member>>> = pathAuthorization
        .clubAuthorization(dto.cid)
        .flatMap {
            LOGGER.info("addMembers: cid: ${dto.cid}; chid: ${dto.chid}")
            memberService.addMembers(dto)
        }
        .invalidateStamp { ResponseStamp.MEMBER.withKey("${dto.chid}") }
        .wrapError()

    @DeleteMapping("/members")
    @Operation(summary = "Removes userId from the members of the clubId")
    fun removeMembers(
        @RequestParam clubId: Long,
        @RequestParam channelId: Long,
        @RequestParam userId: Long,
    ): Mono<ResponseEntity<Void>> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap {
            LOGGER.info("removeMembers: cid: $clubId; chid: $channelId; uid: $userId")
            memberService.removeMember(channelId, userId)
        }
        .invalidateStamp { ResponseStamp.MEMBER.withKey("$channelId") }
        .wrapError()

    @PostMapping
    @Operation(summary = "Creates a channel in the club")
    fun createChannel(@RequestBody channel: Channel): Mono<ResponseEntity<Channel>> = pathAuthorization
        .clubAuthorization(channel.cid)
        .flatMap {
            LOGGER.info("createChannel: channel: $channel")
            channelService.saveChannel(channel)
        }
        .invalidateStamp { ResponseStamp.CHANNEL }
        .wrapError()

    @PutMapping("/{$CHANNEL_ID_CLAIM}")
    @Operation(summary = "Updates the name of channel")
    fun updateChannel(
        @RequestBody dto: UpdateChannelDTO,
        @PathVariable channelId: Long
    ): Mono<ResponseEntity<Channel>> = pathAuthorization
        .clubAuthorization(dto.cid)
        .flatMap {
            LOGGER.info("updateChannel: dto: $dto; chid: $channelId")
            channelService.updateChannel(channelId, dto)
        }
        .invalidateStamp { ResponseStamp.CHANNEL }
        .wrapError()

    @DeleteMapping("/{$CHANNEL_ID_CLAIM}")
    @Operation(summary = "Deletes the channel and the posts in it")
    fun deleteChannel(
        @RequestParam clubId: Long,
        @PathVariable channelId: Long
    ): Mono<ResponseEntity<Void>> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap {
            LOGGER.info("deleteChannel: chid: $channelId; cid: $clubId")
            channelService.deleteChannelByChID(channelId)
        }
        .invalidateStamp { ResponseStamp.CHANNEL }
        .wrapError()
}
