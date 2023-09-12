package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.Post
import com.mnnit.moticlubs.dto.request.UpdatePostDTO
import com.mnnit.moticlubs.repository.PostRepository
import com.mnnit.moticlubs.utils.storeCache
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PostService(
    private val postRepository: PostRepository,
    private val notificationService: NotificationService,
) {

    @CacheEvict("post", allEntries = true)
    fun savePost(post: Post): Mono<Post> = postRepository.save(post)
        .flatMap { savedPost ->
            notificationService.notifyPost(savedPost, false)
                .then(Mono.just(savedPost))
        }

    @Cacheable("post", key = "#chid + '_' + #pageRequest.pageNumber")
    fun getPostsByChannel(chid: Long, pageRequest: PageRequest): Mono<List<Post>> = postRepository
        .findAllByChid(chid, pageRequest)
        .collectList()
        .storeCache()

    @CacheEvict("post", allEntries = true)
    fun updatePost(pid: Long, dto: UpdatePostDTO): Mono<Post> = postRepository
        .updatePost(pid, dto)
        .flatMap { post ->
            notificationService.notifyPost(post, true)
                .then(Mono.just(post))
        }

    @CacheEvict(cacheNames = ["post", "replies"], allEntries = true)
    fun deletePostByPid(pid: Long): Mono<Void> = postRepository
        .findById(pid)
        .flatMap { post ->
            postRepository.deleteById(pid)
                .then(notificationService.notifyDeletePost(post))
        }
}
