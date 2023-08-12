package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.FCM
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class FCMRepository(
    private val db: R2dbcEntityTemplate,
) {

    @Transactional
    fun save(fcm: FCM): Mono<FCM> = db
        .exists(
            Query.query(Criteria.where(fcm::uid.name).`is`(fcm.uid)),
            FCM::class.java
        )
        .flatMap { if (it) update(fcm) else db.insert(fcm) }

    @Transactional
    fun findById(uid: Long): Mono<FCM> = db
        .selectOne(
            Query.query(Criteria.where(FCM::uid.name).`is`(uid)).limit(1),
            FCM::class.java
        )

    @Transactional
    fun findAll(): Flux<FCM> = db.select(Query.empty(), FCM::class.java)

    @Transactional
    fun update(fcm: FCM): Mono<FCM> = db.update(fcm)
}
