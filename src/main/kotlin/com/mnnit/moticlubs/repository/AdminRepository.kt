package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.Admin
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class AdminRepository(
    private val db: R2dbcEntityTemplate,
) {

    @Transactional
    fun save(admin: Admin): Mono<Admin> = exists(admin)
        .flatMap { if (it) Mono.just(admin) else db.insert(admin) }

    @Transactional
    fun findAll(): Flux<Admin> = db.select(Query.empty(), Admin::class.java)

    @Transactional
    fun findAllByCid(cid: Long): Flux<Admin> = db
        .select(
            Query.query(Criteria.where(Admin::cid.name).`is`(cid)),
            Admin::class.java
        )

    @Transactional
    fun exists(admin: Admin): Mono<Boolean> = db
        .exists(
            Query.query(
                Criteria
                    .where(Admin::cid.name)
                    .`is`(admin.cid)
                    .and(
                        Criteria.where(Admin::uid.name).`is`(admin.uid)
                    )
            ),
            Admin::class.java
        )

    @Transactional
    fun delete(admin: Admin): Mono<Void> = db.delete(admin).then()
}
