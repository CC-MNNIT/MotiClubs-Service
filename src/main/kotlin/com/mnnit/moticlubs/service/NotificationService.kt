package com.mnnit.moticlubs.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.mnnit.moticlubs.dao.Post
import com.mnnit.moticlubs.dao.Reply
import com.mnnit.moticlubs.repository.*
import com.mnnit.moticlubs.utils.ServiceLogger
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class NotificationService(
    private val firebaseMessaging: FirebaseMessaging,
    private val fcmRepository: FCMRepository,
    private val subscriberRepository: SubscriberRepository,
    private val replyRepository: ReplyRepository,
    private val clubRepository: ClubRepository,
    private val channelRepository: ChannelRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
) {

    private enum class Type {
        POST, DELETE_POST, REPLY, DELETE_REPLY
    }

    companion object {
        private val LOGGER = ServiceLogger.getLogger(NotificationService::class.java)
    }

    fun notifyPost(post: Post, updated: Boolean): Mono<Void> = Mono
        .zip(
            channelRepository.findById(post.chid),
            userRepository.findById(post.uid),
        )
        .flatMap { tuple ->
            val channel = tuple.t1
            val user = tuple.t2

            clubRepository.findById(channel.cid)
                .flatMap { club ->
                    Mono.just(HashMap<String, String>().apply {
                        this["type"] = Type.POST.ordinal.toString()
                        this["pid"] = post.pid.toString()
                        this["cid"] = channel.cid.toString()
                        this["chid"] = post.chid.toString()
                        this["uid"] = post.uid.toString()
                        this["message"] = post.message
                        this["general"] = post.general.toString()
                        this["adminName"] = user.name
                        this["adminAvatar"] = user.avatar
                        this["clubName"] = club.name
                        this["channelName"] = channel.name
                        this["updated"] = updated.toString()
                    })
                }
        }
        .flatMap { payload ->
            if (post.general == 1) {
                LOGGER.info("notifyPost -> notifyAll")
                notifyAll(payload)
            } else {
                LOGGER.info("notifyPost -> notifySubscribers")
                notifySubscribers(payload["cid"]!!.toLong(), payload)
            }
        }

    fun notifyDeletePost(post: Post): Mono<Void> = Mono.just(post)
        .flatMap { p ->
            Mono.just(HashMap<String, String>().apply {
                this["type"] = Type.DELETE_POST.ordinal.toString()
                this["pid"] = p.pid.toString()
                this["chid"] = p.chid.toString()
            })
        }.flatMap { payload ->
            LOGGER.info("notifyDeletePost: payload: $payload")
            notifyAll(payload)
        }

    fun notifyReply(reply: Reply): Mono<Void> = Mono
        .zip(
            postRepository.findById(reply.pid),
            userRepository.findById(reply.uid),
        )
        .flatMap { tuple ->
            val post = tuple.t1
            val user = tuple.t2

            channelRepository.findById(post.chid)
                .flatMap { channel ->
                    clubRepository.findById(channel.cid)
                        .flatMap { club ->
                            Mono.just(HashMap<String, String>().apply {
                                this["type"] = Type.REPLY.ordinal.toString()
                                this["pid"] = post.pid.toString()
                                this["uid"] = reply.uid.toString()
                                this["message"] = reply.message
                                this["postMessage"] = post.message
                                this["userName"] = user.name
                                this["userAvatar"] = user.avatar
                                this["clubName"] = club.name
                                this["channelName"] = channel.name
                                this["cid"] = club.cid.toString()
                                this["chid"] = channel.chid.toString()
                            })
                        }
                }
        }
        .flatMap { payload ->
            LOGGER.info("notifyReply")
            notifyPostParticipants(reply.pid, payload)
        }

    fun notifyDeleteReply(replyId: Long): Mono<Void> = Mono
        .just(HashMap<String, String>().apply {
            this["type"] = Type.DELETE_REPLY.ordinal.toString()
            this["time"] = replyId.toString()
        })
        .flatMap { payload ->
            LOGGER.info("notifyDeleteReply: payload: $payload")
            notifyAll(payload)
        }

    // --------------------------------------------------------------------- //

    private fun notifySubscribers(cid: Long, payload: HashMap<String, String>): Mono<Void> = Mono
        .from(
            subscriberRepository.findAllByCid(cid)
                .flatMap { fcmRepository.findById(it.uid) }
                .distinct()
                .flatMap { fcm -> sendNotification(fcm.token, payload) }
        )

    private fun notifyPostParticipants(pid: Long, payload: HashMap<String, String>): Mono<Void> = Mono
        .from(
            replyRepository.findUidByPid(pid)
                .distinct()
                .flatMap { fcmRepository.findById(it) }
                .flatMap { fcm -> sendNotification(fcm.token, payload) }
        )

    private fun notifyAll(payload: HashMap<String, String>): Mono<Void> = fcmRepository
        .findAll()
        .flatMap { fcm -> sendNotification(fcm.token, payload) }
        .then()

    private fun sendNotification(token: String, payload: HashMap<String, String>): Mono<Void> = Mono
        .just(
            try {
                val messageId = firebaseMessaging.send(
                    Message.builder()
                        .setToken(token)
                        .putAllData(payload)
                        .build()
                )
                LOGGER.debug("FCM: notification sent: ID: $messageId")
            } catch (e: Exception) {
                LOGGER.warn("Unable to send notification to $token: ${e.localizedMessage}")
            }
        )
        .then()
}
