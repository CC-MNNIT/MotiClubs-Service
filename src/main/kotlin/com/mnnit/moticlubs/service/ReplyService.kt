package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.Reply
import com.mnnit.moticlubs.repository.ReplyRepository
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Service
class ReplyService(
    private val replyRepository: ReplyRepository,
    private val notificationService: NotificationService,
) {

    fun saveReply(reply: Reply): Mono<Reply> = replyRepository.save(reply)
        .flatMap { savedReply ->
            notificationService.notifyReply(savedReply)
                .then(Mono.just(savedReply))
        }

    fun getRepliesByPid(pid: Long, pageRequest: PageRequest): Mono<List<Reply>> = replyRepository
        .findAllByPid(pid, pageRequest)
        .collectList()

    fun deleteReply(uid: Long, time: Long): Mono<Void> = replyRepository.findById(time)
        .flatMap { reply ->
            if (reply.uid != uid) {
                Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not the owner of reply"))
            } else {
                replyRepository.deleteById(time)
                    .then(notificationService.notifyDeleteReply(time))
            }
        }
}
