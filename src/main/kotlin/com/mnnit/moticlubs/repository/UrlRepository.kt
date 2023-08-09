package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.Url
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class UrlRepository(
    private val db: R2dbcEntityTemplate,
) {

    fun saveAll(list: List<Url>): Flux<Url> = Flux.fromIterable(list)
        .concatMap { db.insert(it) }


    fun findAllByCid(cid: Long): Flux<Url> = db
        .select(
            Query.query(Criteria.where(Url::cid.name).`is`(cid)),
            Url::class.java
        )

    fun deleteAllByCid(cid: Long): Mono<Void> = db
        .delete(
            Query.query(Criteria.where(Url::cid.name).`is`(cid)),
            Url::class.java
        )
        .then()
}
