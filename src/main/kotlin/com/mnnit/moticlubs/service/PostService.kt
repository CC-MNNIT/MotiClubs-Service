package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.Post
import com.mnnit.moticlubs.dto.request.UpdatePostDTO
import com.mnnit.moticlubs.repository.PostRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PostService(
    private val postRepository: PostRepository,
    private val notificationService: NotificationService,
) {

    fun savePost(post: Post): Mono<Post> = postRepository.save(post)
        .flatMap { savedPost ->
            notificationService.notifyPost(savedPost, false)
                .then(Mono.just(savedPost))
        }

    fun getPostsByChannel(chid: Long, pageRequest: PageRequest): Mono<List<Post>> = postRepository
        .findAllByChid(chid, pageRequest)
        .collectList()

    fun updatePost(pid: Long, updatePostDTO: UpdatePostDTO): Mono<Post> = postRepository
        .updatePost(pid, updatePostDTO.message)
        .flatMap { post ->
            notificationService.notifyPost(post, true)
                .then(Mono.just(post))
        }

    fun deletePostByPid(pid: Long): Mono<Void> = postRepository
        .deleteById(pid)
        .then(notificationService.notifyDeletePost(pid))
}
