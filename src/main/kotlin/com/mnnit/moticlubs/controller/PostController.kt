package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.Post
import com.mnnit.moticlubs.dto.request.UpdatePostDTO
import com.mnnit.moticlubs.service.PostService
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.POSTS_ROUTE
import com.mnnit.moticlubs.utils.Constants.POST_ID_CLAIM
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/$BASE_PATH/$POSTS_ROUTE")
@Tag(name = "PostRoute")
class PostController(
    private val pathAuthorization: PathAuthorization,
    private val postService: PostService,
) {

    companion object {
        private val LOGGER = ServiceLogger.getLogger(PostController::class.java)
    }

    @GetMapping
    @Operation(summary = "Returns list of posts from the channel")
    fun getPostsByChannel(
        @RequestParam channelId: Long,
        @RequestParam page: Int = 1,
        @RequestParam items: Int = 10,
    ): Mono<List<Post>> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("getPostsByChannel: chid: $channelId; page: $page; items: $items")
            postService.getPostsByChannel(channelId, PageRequest.of(maxOf(page - 1, 0), items))
        }
        .wrapError()

    @PostMapping
    @Operation(summary = "Saves post and notify users")
    fun savePost(
        @RequestBody post: Post,
        @RequestParam clubId: Long
    ): Mono<Post> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap {
            LOGGER.info("savePost: cid: $clubId; post: $post")
            postService.savePost(post)
        }
        .wrapError()

    @PutMapping("/{$POST_ID_CLAIM}")
    @Operation(summary = "Updates post message")
    fun updatePost(
        @RequestBody dto: UpdatePostDTO,
        @PathVariable postId: Long,
        @RequestParam clubId: Long
    ): Mono<Post> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap {
            LOGGER.info("updatePost: pid: $postId; cid: $clubId; dto: $dto")
            postService.updatePost(postId, dto)
        }
        .wrapError()

    @DeleteMapping("/{$POST_ID_CLAIM}")
    @Operation(summary = "Deletes post")
    fun deletePost(
        @PathVariable postId: Long,
        @RequestParam clubId: Long
    ): Mono<Void> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap {
            LOGGER.info("deletePost: pid: $postId; cid: $clubId")
            postService.deletePostByPid(postId)
        }
        .wrapError()
}
