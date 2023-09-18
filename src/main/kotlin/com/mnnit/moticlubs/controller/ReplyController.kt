package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.Reply
import com.mnnit.moticlubs.service.ReplyService
import com.mnnit.moticlubs.utils.Constants
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.REPLY_ROUTE
import com.mnnit.moticlubs.utils.ResponseStamp
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.apiWrapper
import com.mnnit.moticlubs.utils.invalidateStamp
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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
    fun getReplies(
        @RequestParam postId: Long,
        @RequestParam page: Int = 1,
        @RequestParam items: Int = 10,
        @RequestHeader(Constants.STAMP_HEADER) stamp: Long,
    ): Mono<ResponseEntity<List<Reply>>> = apiWrapper(
        key = ResponseStamp.REPLY.withKey("$postId").withKey("$page"),
        stampValue = stamp,
        authorization = pathAuthorization::userAuthorization,
        serviceCall = {
            LOGGER.info("getReplies: pid: $postId; page: $page; items: $items")
            replyService.getRepliesByPid(postId, PageRequest.of(maxOf(page - 1, 0), items))
        },
    )

    @PostMapping
    @Operation(summary = "Saves reply in the post and notify participants")
    fun saveReply(@RequestBody reply: Reply): Mono<ResponseEntity<Reply>> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("saveReply: reply: ${reply.time}")
            replyService.saveReply(reply)
        }
        .invalidateStamp { ResponseStamp.REPLY.withKey("${reply.pid}") }
        .wrapError()

    @DeleteMapping
    @Operation(summary = "Delete reply in the post")
    fun deleteReply(@RequestParam replyId: Long): Mono<ResponseEntity<Void>> = pathAuthorization
        .userAuthorization()
        .flatMap { userId ->
            LOGGER.info("deleteReply: time: $replyId")
            replyService.deleteReply(userId, replyId)
        }
        .invalidateStamp { ResponseStamp.REPLY }
        .wrapError()
}
