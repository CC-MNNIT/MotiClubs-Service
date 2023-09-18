package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.Channel
import com.mnnit.moticlubs.dto.request.UpdateChannelDTO
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Update
import org.springframework.data.relational.core.sql.SqlIdentifier
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
    fun findAll(uid: Long): Flux<Channel> = db
        .databaseClient
        .sql(
            "SELECT channel.* FROM channel INNER JOIN member ON channel.chid = member.chid " +
                "WHERE uid = :uid UNION (SELECT * FROM channel WHERE private = 0) ORDER BY chid",
        )
        .bind(0, uid)
        .fetch()
        .all()
        .map { Channel(it) }

    @Transactional
    fun findById(chid: Long): Mono<Channel> = db
        .selectOne(
            Query.query(Criteria.where(Channel::chid.name).`is`(chid)).limit(1),
            Channel::class.java,
        )

    @Transactional
    fun findByCid(cid: Long): Flux<Channel> = db
        .select(
            Query.query(Criteria.where(Channel::cid.name).`is`(cid)),
            Channel::class.java,
        )

    @Transactional
    fun update(chid: Long, dto: UpdateChannelDTO): Mono<Channel> = db
        .update(
            Query.query(Criteria.where(Channel::chid.name).`is`(chid)),
            Update.from(
                HashMap<SqlIdentifier, Any>().apply {
                    this[SqlIdentifier.unquoted(Channel::name.name)] = dto.name
                    this[SqlIdentifier.unquoted(Channel::private.name)] = if (dto.private) 1 else 0
                },
            ),
            Channel::class.java,
        )
        .flatMap { findById(chid) }

    @Transactional
    fun deleteById(chid: Long): Mono<Void> = db
        .delete(
            Query.query(Criteria.where(Channel::chid.name).`is`(chid)),
            Channel::class.java,
        )
        .then()
}
