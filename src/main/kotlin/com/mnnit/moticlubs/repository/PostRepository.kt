package com.mnnit.moticlubs.repository

import com.mnnit.moticlubs.dao.Post
import org.springframework.data.domain.PageRequest
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class PostRepository(
    private val db: R2dbcEntityTemplate,
) {

    fun save(post: Post): Mono<Post> = db.insert(post)

    fun findById(pid: Long): Mono<Post> = db
        .selectOne(
            Query.query(Criteria.where(Post::pid.name).`is`(pid)).limit(1),
            Post::class.java
        )

    fun findAllByChid(chid: Long, pageRequest: PageRequest): Flux<Post> = db
        .select(
            Query.query(Criteria.where(Post::chid.name).`is`(chid))
                .limit(pageRequest.pageSize)
                .offset(pageRequest.offset),
            Post::class.java
        )

    fun updatePost(pid: Long, message: String): Mono<Post> = db
        .update(
            Query.query(Criteria.where(Post::pid.name).`is`(pid)),
            Update.update(Post::message.name, message),
            Post::class.java
        )
        .then(findById(pid))

    fun deleteById(pid: Long): Mono<Void> = db
        .delete(
            Query.query(Criteria.where(Post::pid.name).`is`(pid)),
            Post::class.java
        )
        .then()

    fun deleteAllByChid(chid: Long): Mono<Void> = db
        .delete(
            Query.query(Criteria.where(Post::chid.name).`is`(chid)),
            Post::class.java
        )
        .then()
}
