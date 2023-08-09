package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.Club
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ClubRepository(
    private val db: R2dbcEntityTemplate,
) {

    fun save(club: Club): Mono<Club> = db.insert(club)

    fun findAll(): Flux<Club> = db.select(Query.empty(), Club::class.java)

    fun findById(cid: Long): Mono<Club> = db
        .selectOne(
            Query.query(Criteria.where(Club::cid.name).`is`(cid)).limit(1),
            Club::class.java
        )

    fun existsById(cid: Long): Mono<Boolean> = db
        .exists(
            Query.query(Criteria.where(Club::cid.name).`is`(cid)),
            Club::class.java
        )

    fun deleteById(cid: Long): Mono<Void> = db
        .delete(
            Query.query(Criteria.where(Club::cid.name).`is`(cid)),
            Club::class.java
        )
        .then()
}
