package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.SuperAdmin
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Repository
class SuperAdminRepository(
    private val db: R2dbcEntityTemplate,
) {

    @Transactional
    fun existsById(uid: Long): Mono<Boolean> = db
        .exists(
            Query.query(Criteria.where(SuperAdmin::uid.name).`is`(uid)),
            SuperAdmin::class.java,
        )
}
