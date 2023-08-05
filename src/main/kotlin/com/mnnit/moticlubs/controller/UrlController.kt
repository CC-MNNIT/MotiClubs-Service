package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dto.Url
import com.mnnit.moticlubs.dto.request.SaveUrlsDTO
import com.mnnit.moticlubs.service.UrlService
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.URL_ROUTE
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/$BASE_PATH/$URL_ROUTE")
@Tag(name = "UrlRoute")
class UrlController(
    private val pathAuthorization: PathAuthorization,
    private val urlService: UrlService,
) {

    @GetMapping
    @Operation(summary = "Returns list of urls for the club")
    fun getUrls(@RequestParam clubId: Long): Mono<List<Url>> = pathAuthorization
        .userAuthorization()
        .flatMap { urlService.getUrlsByCid(clubId) }
        .wrapError()

    @PostMapping
    @Operation(summary = "Updates the list of urls for the club")
    fun updateUrls(@RequestBody dto: SaveUrlsDTO, @RequestParam clubId: Long): Mono<List<Url>> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap { urlService.saveUrl(clubId, dto.urls) }
        .wrapError()
}
