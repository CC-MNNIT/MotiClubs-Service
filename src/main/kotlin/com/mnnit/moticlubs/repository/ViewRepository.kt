package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.View
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ViewRepository(
    private val db: R2dbcEntityTemplate,
) {

    @Transactional
    fun save(view: View): Mono<View> = db
        .exists(
            Query.query(
                Criteria
                    .where(View::pid.name)
                    .`is`(view.pid)
                    .and(
                        Criteria.where(View::uid.name).`is`(view.uid)
                    )
            ),
            View::class.java
        )
        .flatMap { if (it) Mono.just(view) else db.insert(view) }

    @Transactional
    fun findAllByPid(pid: Long): Flux<View> = db
        .select(
            Query.query(Criteria.where(View::pid.name).`is`(pid)),
            View::class.java
        )
}
