package com.mnnit.moticlubs.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.mnnit.moticlubs.dao.Channel
import com.mnnit.moticlubs.dao.Club
import com.mnnit.moticlubs.dao.FCM
import com.mnnit.moticlubs.dao.Post
import com.mnnit.moticlubs.dao.Reply
import com.mnnit.moticlubs.dao.User
import com.mnnit.moticlubs.repository.ChannelRepository
import com.mnnit.moticlubs.repository.ClubRepository
import com.mnnit.moticlubs.repository.FCMRepository
import com.mnnit.moticlubs.repository.MemberRepository
import com.mnnit.moticlubs.repository.PostRepository
import com.mnnit.moticlubs.repository.ReplyRepository
import com.mnnit.moticlubs.repository.UserRepository
import com.mnnit.moticlubs.utils.ServiceLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.util.function.Tuples

@Service
class NotificationService(
    private val firebaseMessaging: FirebaseMessaging,
    private val fcmRepository: FCMRepository,
    private val memberRepository: MemberRepository,
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

    fun notifyPost(post: Post, updated: Boolean) = coroutineWrapper {
        Mono
            .zip(
                channelRepository.findById(post.chid),
                userRepository.findById(post.uid),
            )
            .flatMap { tuple ->
                val channel = tuple.t1
                val user = tuple.t2

                clubRepository.findById(channel.cid)
                    .flatMap { club ->
                        Mono.just(
                            getPostPayload(post, user, club, channel).apply {
                                this["type"] = Type.POST.ordinal.toString()
                                this["updated"] = updated.toString()
                            },
                        )
                    }
                    .flatMap { payload ->
                        if (channel.private) {
                            LOGGER.info("notifyPost -> notifySubscribers")
                            notifyMembers(channel.chid, payload)
                        } else {
                            LOGGER.info("notifyPost -> notifyAll")
                            notifyAll(payload)
                        }
                    }
            }
            .subscribe()
    }

    fun notifyDeletePost(post: Post) = coroutineWrapper {
        Mono.just(post)
            .flatMap { p ->
                Mono.just(
                    HashMap<String, String>().apply {
                        this["type"] = Type.DELETE_POST.ordinal.toString()
                        this["pid"] = p.pid.toString()
                        this["chid"] = p.chid.toString()
                    },
                )
            }
            .flatMap { payload ->
                LOGGER.info("notifyDeletePost: payload: $payload")
                notifyAll(payload)
            }
            .subscribe()
    }

    fun notifyReply(reply: Reply) = coroutineWrapper {
        Mono
            .zip(
                postRepository.findById(reply.pid),
                userRepository.findById(reply.uid),
            )
            .flatMap { tuple ->
                val post = tuple.t1
                val user = tuple.t2
                channelRepository.findById(post.chid).map { channel -> Tuples.of(post, user, channel) }
            }
            .flatMap { tuple ->
                clubRepository.findById(tuple.t3.cid).map { club -> Tuples.of(tuple.t1, tuple.t2, tuple.t3, club) }
            }
            .map { getReplyPayload(it.t1, it.t2, reply, it.t4, it.t3) }
            .flatMap { payload ->
                LOGGER.info("notifyReply: ${reply.pid}")
                notifyPostParticipants(reply.pid, payload)
            }
            .subscribe()
    }

    fun notifyDeleteReply(reply: Reply) = coroutineWrapper {
        postRepository.findById(reply.pid)
            .flatMap { post ->
                Mono.just(
                    HashMap<String, String>().apply {
                        this["type"] = Type.DELETE_REPLY.ordinal.toString()
                        this["pid"] = reply.pid.toString()
                        this["chid"] = post.chid.toString()
                        this["time"] = reply.time.toString()
                    },
                )
            }
            .flatMap { payload ->
                LOGGER.info("notifyDeleteReply: payload: $payload")
                notifyAll(payload)
            }
            .subscribe()
    }

    // --------------------------------------------------------------------- //

    private fun coroutineWrapper(func: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch { func() }
    }

    private fun getPostPayload(
        post: Post,
        user: User,
        club: Club,
        channel: Channel,
    ): HashMap<String, String> = HashMap<String, String>().apply {
        putAll(post.toHashMap())
        putAll(channel.toHashMap())
        putAll(club.toHashMap())
        putAll(user.toHashMap())
    }

    private fun getReplyPayload(
        post: Post,
        user: User,
        reply: Reply,
        club: Club,
        channel: Channel,
    ): HashMap<String, String> = getPostPayload(post, user, club, channel).apply {
        this["type"] = Type.REPLY.ordinal.toString()

        putAll(reply.toHashMap())
    }

    private fun notifyMembers(chid: Long, payload: HashMap<String, String>): Mono<Void> = Mono
        .from(
            memberRepository.findAllByChid(chid)
                .flatMap { fcmRepository.findById(it.uid) }
                .distinct { it.uid }
                .flatMap { fcm -> sendNotification(fcm, payload) },
        )

    private fun notifyPostParticipants(pid: Long, payload: HashMap<String, String>): Mono<Void> = Mono
        .from(
            replyRepository.findUidByPid(pid)
                .distinct()
                .flatMap { fcmRepository.findById(it) }
                .flatMap { fcm -> sendNotification(fcm, payload) },
        )

    private fun notifyAll(payload: HashMap<String, String>): Mono<Void> = fcmRepository
        .findAll()
        .flatMap { fcm -> sendNotification(fcm, payload) }
        .then()

    private fun sendNotification(fcm: FCM, payload: HashMap<String, String>): Mono<Void> = Mono
        .just(
            try {
                val messageId = firebaseMessaging.send(
                    Message.builder()
                        .setToken(fcm.token)
                        .putAllData(payload)
                        .build(),
                )
                LOGGER.debug("FCM: notification sent: ID: $messageId")
            } catch (e: Exception) {
                LOGGER.warn("Unable to send notification to ${fcm.uid}: ${e.localizedMessage}")
            },
        )
        .then()
}
