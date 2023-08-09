package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.Club
import com.mnnit.moticlubs.dto.request.UpdateClubDTO
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Update
import org.springframework.data.relational.core.sql.SqlIdentifier
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
        .then(findById(cid))


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
