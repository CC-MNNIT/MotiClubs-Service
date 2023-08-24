package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.Member
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class MemberRepository(
    private val db: R2dbcEntityTemplate,
) {

    @Transactional
    fun save(member: Member): Mono<Member> = exists(member)
        .flatMap { if (it) Mono.just(member) else db.insert(member) }

    @Transactional
    fun findAllByChid(chid: Long): Flux<Member> = db
        .select(
            Query.query(Criteria.where(Member::chid.name).`is`(chid)),
            Member::class.java
        )

    @Transactional
    fun exists(member: Member): Mono<Boolean> = db
        .exists(
            Query.query(
                Criteria
                    .where(Member::chid.name).`is`(member.chid)
                    .and(
                        Criteria.where(Member::uid.name).`is`(member.uid)
                    )
            ),
            Member::class.java
        )

    @Transactional
    fun delete(member: Member): Mono<Void> = exists(member)
        .flatMap { if (it) db.delete(member) else Mono.just(member) }
        .then()

    @Transactional
    fun deleteAllByUid(uid: Long): Mono<Void> = db
        .delete(
            Query.query(Criteria.where(Member::uid.name).`is`(uid)),
            Member::class.java
        )
        .then()

    @Transactional
    fun deleteAllByChid(chid: Long): Mono<Void> = db
        .delete(
            Query.query(Criteria.where(Member::chid.name).`is`(chid)),
            Member::class.java
        )
        .then()
}
