package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.Admin
import com.mnnit.moticlubs.dao.User
import com.mnnit.moticlubs.dto.response.AdminUserDTO
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
    fun findAll(): Flux<AdminUserDTO> = db
        .databaseClient
        .sql("SELECT admin.cid, user.* FROM admin INNER JOIN user ON admin.uid = user.uid ORDER BY user.name")
        .fetch()
        .all()
        .map {
            AdminUserDTO(
                clubId = it[Admin::cid.name].toString().toLong(),
                user = User(it)
            )
        }

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
