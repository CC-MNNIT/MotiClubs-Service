package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.PostRepository
import com.mnnit.moticlubs.dto.Post
import com.mnnit.moticlubs.dto.request.SavePostDTO
import com.mnnit.moticlubs.dto.request.UpdatePostDTO
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PostService(
    private val postRepository: PostRepository,
    private val notificationService: NotificationService,
) {

    fun savePost(dto: SavePostDTO): Mono<Post> = postRepository.save(Post(dto))
        .flatMap { savedPost ->
            notificationService.notifyPost(savedPost, false)
                .then(Mono.just(savedPost))
        }

    fun getPostByPid(pid: Long): Mono<Post> = postRepository.findById(pid)

    fun getPostsByChannel(chid: Long): Mono<List<Post>> = postRepository
        .findAllByChid(chid)
        .collectList()

    fun updatePost(pid: Long, updatePostDTO: UpdatePostDTO): Mono<Post> = postRepository
        .findById(pid)
        .flatMap { postRepository.save(it.copy(message = updatePostDTO.message)) }
        .flatMap { post ->
            notificationService.notifyPost(post, true)
                .then(Mono.just(post))
        }

    fun deletePostByPid(pid: Long): Mono<Void> = postRepository
        .deleteById(pid)
        .then(notificationService.notifyDeletePost(pid))
}
