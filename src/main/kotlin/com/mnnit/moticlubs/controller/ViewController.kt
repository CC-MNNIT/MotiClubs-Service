package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.View
import com.mnnit.moticlubs.service.ViewService
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.VIEWS_ROUTE
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/$BASE_PATH/$VIEWS_ROUTE")
@Tag(name = "ViewRoute")
class ViewController(
    private val pathAuthorization: PathAuthorization,
    private val viewService: ViewService,
) {

    companion object {
        private val LOGGER = ServiceLogger.getLogger(ViewController::class.java)
    }

    @GetMapping
    @Operation(summary = "Get number of views of a post")
    fun getViews(@RequestParam postId: Long): Mono<List<View>> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("getViews: pid: $postId")
            viewService.getViewsByPid(postId)
        }
        .wrapError()

    @PostMapping
    @Operation(summary = "Add views of a post")
    fun addView(@RequestBody view: View): Mono<View> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("addView: view: $view")
            viewService.saveView(view)
        }
        .wrapError()
}
