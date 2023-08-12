package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.Reply
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ReplyRepository(
    private val db: R2dbcEntityTemplate,
) {

    @Transactional
    fun save(reply: Reply): Mono<Reply> = db.insert(reply)

    @Transactional
    fun findById(rid: Long): Mono<Reply> = db
        .selectOne(
            Query.query(Criteria.where(Reply::time.name).`is`(rid)).limit(1),
            Reply::class.java
        )

    @Transactional
    fun findAllByPid(pid: Long): Flux<Reply> = db
        .select(
            Query.query(Criteria.where(Reply::pid.name).`is`(pid)),
            Reply::class.java
        )

    @Transactional
    fun deleteById(rid: Long): Mono<Void> = db
        .delete(
            Query.query(Criteria.where(Reply::time.name).`is`(rid)),
            Reply::class.java
        )
        .then()
}
