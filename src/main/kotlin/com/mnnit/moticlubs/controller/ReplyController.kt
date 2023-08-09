package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.Reply
import com.mnnit.moticlubs.service.ReplyService
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.REPLY_ROUTE
import com.mnnit.moticlubs.utils.ServiceLogger
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

    companion object {
        private val LOGGER = ServiceLogger.getLogger(ReplyController::class.java)
    }

    @GetMapping
    @Operation(summary = "Returns replies from the post")
    fun getReplies(@RequestParam postId: Long): Mono<List<Reply>> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("getReplies: pid: $postId")
            replyService.getRepliesByPid(postId)
        }
        .wrapError()

    @PostMapping
    @Operation(summary = "Saves reply in the post and notify participants")
    fun saveReply(@RequestBody reply: Reply): Mono<Reply> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("saveReply: reply: $reply")
            replyService.saveReply(reply)
        }
        .wrapError()

    @DeleteMapping
    @Operation(summary = "Delete reply in the post")
    fun deleteReply(@RequestParam replyId: Long): Mono<Void> = pathAuthorization
        .userAuthorization()
        .flatMap { userId ->
            LOGGER.info("deleteReply: time: $replyId")
            replyService.deleteReply(userId, replyId)
        }
        .wrapError()
}
