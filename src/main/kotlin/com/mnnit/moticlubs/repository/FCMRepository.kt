package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.FCM
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class FCMRepository(
    private val db: R2dbcEntityTemplate,
) {

    fun save(fcm: FCM): Mono<FCM> = db.insert(fcm)

    fun findById(uid: Long): Mono<FCM> = db
        .selectOne(
            Query.query(Criteria.where(FCM::uid.name).`is`(uid)).limit(1),
            FCM::class.java
        )

    fun findAll(): Flux<FCM> = db.select(Query.empty(), FCM::class.java)

    fun update(fcm: FCM): Mono<FCM> = db.update(fcm)
}
