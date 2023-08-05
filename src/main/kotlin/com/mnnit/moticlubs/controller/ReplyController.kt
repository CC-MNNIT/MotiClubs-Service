package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dto.Reply
import com.mnnit.moticlubs.service.ReplyService
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.REPLY_ROUTE
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/$BASE_PATH/$REPLY_ROUTE")
@Tag(name = "ReplyRoute")
class ReplyController(
    private val pathAuthorization: PathAuthorization,
    private val replyService: ReplyService,
) {

    @GetMapping
    @Operation(summary = "Returns replies from the post")
    fun getReplies(@RequestParam postId: Long): Mono<List<Reply>> = pathAuthorization
        .userAuthorization()
        .flatMap { replyService.getRepliesByPid(postId) }
        .wrapError()

    @PostMapping
    @Operation(summary = "Saves reply in the post and notify participants")
    fun saveReply(@RequestBody reply: Reply): Mono<Reply> = pathAuthorization
        .userAuthorization()
        .flatMap { replyService.saveReply(reply) }
        .wrapError()

    @DeleteMapping
    @Operation(summary = "Delete reply in the post")
    fun deleteReply(@RequestParam replyId: Long): Mono<Void> = pathAuthorization
        .userAuthorization()
        .flatMap { userId -> replyService.deleteReply(userId, replyId) }
        .wrapError()
}
