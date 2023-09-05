package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.Club
import com.mnnit.moticlubs.dto.request.UpdateClubDTO
import org.springframework.data.domain.Sort
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
class ClubRepository(
    private val db: R2dbcEntityTemplate,
) {

    @Transactional
    fun save(club: Club): Mono<Club> = db.insert(club)

    @Transactional
    fun findAll(): Flux<Club> = db.select(
        Query.empty().sort(Sort.by(Sort.Direction.ASC, Club::cid.name)),
        Club::class.java
    )

    @Transactional
    fun findById(cid: Long): Mono<Club> = db
        .selectOne(
            Query.query(Criteria.where(Club::cid.name).`is`(cid)).limit(1),
            Club::class.java
        )

    @Transactional
    fun updateClub(cid: Long, dto: UpdateClubDTO): Mono<Club> = db
        .update(
            Query.query(Criteria.where(Club::cid.name).`is`(cid)),
            Update.from(HashMap<SqlIdentifier, Any>().apply {
                this[SqlIdentifier.unquoted(Club::description.name)] = dto.description
                this[SqlIdentifier.unquoted(Club::avatar.name)] = dto.avatar
                this[SqlIdentifier.unquoted(Club::summary.name)] = dto.summary
            }),
            Club::class.java
        )
        .flatMap { findById(cid) }

    @Transactional
    fun existsById(cid: Long): Mono<Boolean> = db
        .exists(
            Query.query(Criteria.where(Club::cid.name).`is`(cid)),
            Club::class.java
        )

    @Transactional
    fun deleteById(cid: Long): Mono<Void> = db
        .delete(
            Query.query(Criteria.where(Club::cid.name).`is`(cid)),
            Club::class.java
        )
        .then()
}
