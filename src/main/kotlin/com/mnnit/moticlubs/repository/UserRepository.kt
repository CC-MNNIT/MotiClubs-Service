package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.User
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Repository
class UserRepository(
    private val db: R2dbcEntityTemplate,
) {

    @Transactional
    fun save(user: User): Mono<User> = db.insert(user)

    @Transactional
    fun findById(uid: Long): Mono<User> = db
        .selectOne(
            Query.query(Criteria.where(User::uid.name).`is`(uid)).limit(1),
            User::class.java
        )

    @Transactional
    fun findByRegNo(regNo: String): Mono<User> = db
        .selectOne(
            Query.query(Criteria.where(User::regno.name).`is`(regNo)).limit(1),
            User::class.java
        )

    @Transactional
    fun deleteById(uid: Long): Mono<Void> = db
        .delete(
            Query.query(Criteria.where(User::uid.name).`is`(uid)).limit(1),
            User::class.java
        )
        .then()
}
