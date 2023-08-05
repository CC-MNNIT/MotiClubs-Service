package com.mnnit.moticlubs.dao

import com.mnnit.moticlubs.dto.*
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AdminRepository : R2dbcRepository<Admin, Long>

interface ChannelRepository : R2dbcRepository<Channel, Long> {

    fun findChannelsByCid(cid: Long): Flux<Channel>
}

interface ClubRepository : R2dbcRepository<Club, Long>

interface FCMRepository : R2dbcRepository<FCM, Long>

interface PostRepository : R2dbcRepository<Post, Long> {

    fun findAllByChid(chid: Long): Flux<Post>
    fun deleteAllByChid(chid: Long): Mono<Void>
}

interface UserRepository : R2dbcRepository<User, Long> {

    fun findUserByEmail(email: String): Mono<User>
}

interface ReplyRepository : R2dbcRepository<Reply, Long> {

    fun findAllByPid(pid: Long): Flux<Reply>
}

interface SubscriberRepository : R2dbcRepository<Subscriber, Long> {

    fun findAllByCid(cid: Long): Flux<Subscriber>
}

interface SuperAdminRepository : R2dbcRepository<SuperAdmin, Long>

interface UrlRepository : R2dbcRepository<Url, Long> {

    fun findAllByCid(cid: Long): Flux<Url>
}

interface ViewRepository : R2dbcRepository<View, Long> {

    fun findAllByPid(pid: Long): Flux<View>
}
