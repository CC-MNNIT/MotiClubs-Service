package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.Channel
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ChannelRepository(
    private val db: R2dbcEntityTemplate,
) {

    @Transactional
    fun save(channel: Channel): Mono<Channel> = db.insert(channel)

    @Transactional
    fun findAll(): Flux<Channel> = db.select(Query.empty(), Channel::class.java)

    @Transactional
    fun findById(chid: Long): Mono<Channel> = db
        .selectOne(
            Query.query(Criteria.where(Channel::chid.name).`is`(chid)).limit(1),
            Channel::class.java
        )

    @Transactional
    fun updateName(chid: Long, name: String): Mono<Channel> = db
        .update(
            Query.query(Criteria.where(Channel::chid.name).`is`(chid)),
            Update.update(Channel::name.name, name),
            Channel::class.java
        )
        .then(findById(chid))

    @Transactional
    fun deleteById(chid: Long): Mono<Void> = db
        .delete(
            Query.query(Criteria.where(Channel::chid.name).`is`(chid)),
            Channel::class.java
        ).then()
}
