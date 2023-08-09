package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.Url
import com.mnnit.moticlubs.dto.request.SaveUrlsDTO
import com.mnnit.moticlubs.service.UrlService
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.URL_ROUTE
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/$BASE_PATH/$URL_ROUTE")
@Tag(name = "UrlRoute")
class UrlController(
    private val pathAuthorization: PathAuthorization,
    private val urlService: UrlService,
) {

    companion object {
        private val LOGGER = ServiceLogger.getLogger(UrlController::class.java)
    }

    @GetMapping
    @Operation(summary = "Returns list of urls for the club")
    fun getUrls(@RequestParam clubId: Long): Mono<List<Url>> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("getUrls: cid: $clubId")
            urlService.getUrlsByCid(clubId)
        }
        .wrapError()

    @PostMapping
    @Operation(summary = "Updates the list of urls for the club")
    fun updateUrls(@RequestBody dto: SaveUrlsDTO, @RequestParam clubId: Long): Mono<List<Url>> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap {
            LOGGER.info("updateUrls: cid: $clubId")
            urlService.saveUrl(
                clubId,
                dto.urls.map { url -> Url(url.urlid, clubId, url.name, url.color, url.url) }
            )
        }
        .wrapError()
}
