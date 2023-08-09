package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.Subscribers
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class SubscriberRepository(
    private val db: R2dbcEntityTemplate,
) {

    fun save(subscribers: Subscribers): Mono<Subscribers> = db.insert(subscribers)

    fun findAllByCid(cid: Long): Flux<Subscribers> = db
        .select(
            Query.query(Criteria.where(Subscribers::cid.name).`is`(cid)),
            Subscribers::class.java
        )

    fun findAllByUid(uid: Long): Flux<Subscribers> = db
        .select(
            Query.query(Criteria.where(Subscribers::uid.name).`is`(uid)),
            Subscribers::class.java
        )

    fun delete(subscribers: Subscribers): Mono<Void> = db.delete(subscribers).then()
}
