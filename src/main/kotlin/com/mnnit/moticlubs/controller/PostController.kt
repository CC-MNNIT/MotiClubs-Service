package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.Post
import com.mnnit.moticlubs.dao.View
import com.mnnit.moticlubs.dto.request.UpdatePostDTO
import com.mnnit.moticlubs.service.PostService
import com.mnnit.moticlubs.service.ViewService
import com.mnnit.moticlubs.utils.Constants
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.POSTS_ROUTE
import com.mnnit.moticlubs.utils.Constants.POST_ID_CLAIM
import com.mnnit.moticlubs.utils.Constants.VIEWS_ROUTE
import com.mnnit.moticlubs.utils.ResponseStamp
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.apiWrapper
import com.mnnit.moticlubs.utils.invalidateStamp
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
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
    private val viewService: ViewService,
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
        @RequestHeader(Constants.STAMP_HEADER) stamp: Long,
    ): Mono<ResponseEntity<List<Post>>> = apiWrapper(
        key = ResponseStamp.POST.withKey("$channelId").withKey("$page"),
        stampValue = stamp,
        authorization = pathAuthorization::userAuthorization,
        serviceCall = {
            LOGGER.info("getPostsByChannel: chid: $channelId; page: $page; items: $items")
            postService.getPostsByChannel(channelId, PageRequest.of(maxOf(page - 1, 0), items))
        },
    )

    @PostMapping
    @Operation(summary = "Saves post and notify users")
    fun savePost(
        @RequestBody post: Post,
        @RequestParam clubId: Long,
    ): Mono<ResponseEntity<Post>> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap {
            LOGGER.info("savePost: cid: $clubId")
            postService.savePost(post)
        }
        .invalidateStamp { ResponseStamp.POST.withKey("${post.chid}") }
        .wrapError()

    @PutMapping("/{$POST_ID_CLAIM}")
    @Operation(summary = "Updates post message")
    fun updatePost(
        @RequestBody dto: UpdatePostDTO,
        @PathVariable postId: Long,
        @RequestParam clubId: Long,
    ): Mono<ResponseEntity<Post>> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap {
            LOGGER.info("updatePost: pid: $postId; cid: $clubId")
            postService.updatePost(postId, dto)
        }
        .invalidateStamp { ResponseStamp.POST.withKey("${it.chid}") }
        .wrapError()

    @DeleteMapping("/{$POST_ID_CLAIM}")
    @Operation(summary = "Deletes post")
    fun deletePost(
        @PathVariable postId: Long,
        @RequestParam clubId: Long,
    ): Mono<ResponseEntity<Void>> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap {
            LOGGER.info("deletePost: pid: $postId; cid: $clubId")
            postService.deletePostByPid(postId)
        }
        .invalidateStamp { ResponseStamp.POST }
        .wrapError()

    @GetMapping("/$VIEWS_ROUTE")
    @Operation(summary = "Get number of views of a post")
    fun getViews(@RequestParam postId: Long): Mono<List<View>> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("getViews: pid: $postId")
            viewService.getViewsByPid(postId)
        }
        .wrapError()

    @PostMapping("/$VIEWS_ROUTE")
    @Operation(summary = "Add views of a post")
    fun addView(@RequestBody view: View): Mono<View> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("addView: view: $view")
            viewService.saveView(view)
        }
        .wrapError()
}
