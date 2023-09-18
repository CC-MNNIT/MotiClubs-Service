package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.User
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class UserRepository(
    private val db: R2dbcEntityTemplate,
) {

    @Transactional
    fun save(user: User): Mono<User> = exists(user)
        .flatMap { if (it) findByRegNo(user.regno) else db.insert(user) }

    @Transactional
    fun findById(uid: Long): Mono<User> = db
        .selectOne(
            Query.query(Criteria.where(User::uid.name).`is`(uid)).limit(1),
            User::class.java,
        )

    @Transactional
    fun findAll(): Flux<User> = db.select(Query.empty(), User::class.java)

    @Transactional
    fun findByRegNo(regNo: String): Mono<User> = db
        .selectOne(
            Query.query(Criteria.where(User::regno.name).`is`(regNo)).limit(1),
            User::class.java,
        )

    @Transactional
    fun exists(user: User): Mono<Boolean> = db
        .exists(
            Query.query(Criteria.where(User::email.name).`is`(user.email)),
            User::class.java,
        )

    @Transactional
    fun updateAvatar(uid: Long, avatar: String): Mono<User> = db
        .update(
            Query.query(Criteria.where(User::uid.name).`is`(uid)),
            Update.update(User::avatar.name, avatar),
            User::class.java,
        )
        .then(findById(uid))

    @Transactional
    fun updateContact(uid: Long, contact: String): Mono<User> = db
        .update(
            Query.query(Criteria.where(User::uid.name).`is`(uid)),
            Update.update(User::contact.name, contact),
            User::class.java,
        )
        .then(findById(uid))

    @Transactional
    fun deleteById(uid: Long): Mono<Void> = db
        .delete(
            Query.query(Criteria.where(User::uid.name).`is`(uid)).limit(1),
            User::class.java,
        )
        .then()
}
