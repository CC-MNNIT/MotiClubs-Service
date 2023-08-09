package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.View
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ViewRepository(
    private val db: R2dbcEntityTemplate,
) {

    fun save(view: View): Mono<View> = db.insert(view)

    fun findAllByPid(pid: Long): Flux<View> = db
        .select(
            Query.query(Criteria.where(View::pid.name).`is`(pid)),
            View::class.java
        )
}
