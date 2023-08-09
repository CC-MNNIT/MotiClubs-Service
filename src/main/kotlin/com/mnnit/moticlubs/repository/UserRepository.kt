package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.User
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class UserRepository(
    private val db: R2dbcEntityTemplate,
) {

    fun save(user: User): Mono<User> = db.insert(user)

    fun findById(uid: Long): Mono<User> = db
        .selectOne(
            Query.query(Criteria.where(User::uid.name).`is`(uid)).limit(1),
            User::class.java
        )

    fun findByEmail(email: String): Mono<User> = db
        .selectOne(
            Query.query(Criteria.where(User::email.name).`is`(email)).limit(1),
            User::class.java
        )

    fun deleteById(uid: Long): Mono<Void> = db
        .delete(
            Query.query(Criteria.where(User::uid.name).`is`(uid)).limit(1),
            User::class.java
        )
        .then()
}
