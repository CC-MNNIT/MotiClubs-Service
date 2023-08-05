package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dto.Post
import com.mnnit.moticlubs.dto.request.SavePostDTO
import com.mnnit.moticlubs.dto.request.UpdatePostDTO
import com.mnnit.moticlubs.service.PostService
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.POSTS_ROUTE
import com.mnnit.moticlubs.utils.Constants.POST_ID_CLAIM
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/$BASE_PATH/$POSTS_ROUTE")
@Tag(name = "PostRoute")
class PostController(
    private val pathAuthorization: PathAuthorization,
    private val postService: PostService,
) {

    @GetMapping
    @Operation(summary = "Returns list of posts from the channel")
    fun getPostsByChannel(@RequestParam channelId: Long): Mono<List<Post>> = pathAuthorization
        .userAuthorization()
        .flatMap { postService.getPostsByChannel(channelId) }
        .wrapError()

    @PostMapping
    @Operation(summary = "Saves post and notify users")
    fun savePost(
        @RequestBody dto: SavePostDTO,
        @RequestParam clubId: Long
    ): Mono<Post> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap { postService.savePost(dto) }
        .wrapError()

    @PutMapping("/{$POST_ID_CLAIM}")
    @Operation(summary = "Updates post message")
    fun updatePost(
        @RequestBody dto: UpdatePostDTO,
        @PathVariable postId: Long,
        @RequestParam clubId: Long
    ): Mono<Post> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap { postService.updatePost(postId, dto) }
        .wrapError()

    @DeleteMapping("/{$POST_ID_CLAIM}")
    @Operation(summary = "Deletes post")
    fun deletePost(
        @PathVariable postId: Long,
        @RequestParam clubId: Long
    ): Mono<Void> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap { postService.deletePostByPid(postId) }
        .wrapError()
}
