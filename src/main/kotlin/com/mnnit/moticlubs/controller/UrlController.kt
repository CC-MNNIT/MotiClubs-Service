package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.Url
import com.mnnit.moticlubs.dto.request.SaveUrlsDTO
import com.mnnit.moticlubs.service.UrlService
import com.mnnit.moticlubs.utils.Constants
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.URL_ROUTE
import com.mnnit.moticlubs.utils.ResponseStamp
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.apiWrapper
import com.mnnit.moticlubs.utils.invalidateStamp
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
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

    companion object {
        private val LOGGER = ServiceLogger.getLogger(UrlController::class.java)
    }

    @GetMapping
    @Operation(summary = "Returns list of urls for the club")
    fun getUrls(
        @RequestParam clubId: Long,
        @RequestHeader(Constants.STAMP_HEADER) stamp: Long,
    ): Mono<ResponseEntity<List<Url>>> = apiWrapper(
        key = ResponseStamp.URL.withKey("$clubId"),
        stampValue = stamp,
        authorization = pathAuthorization::userAuthorization,
        serviceCall = {
            LOGGER.info("getUrls: cid: $clubId")
            urlService.getUrlsByCid(clubId)
        },
    )

    @PostMapping
    @Operation(summary = "Updates the list of urls for the club")
    fun updateUrls(@RequestBody dto: SaveUrlsDTO, @RequestParam clubId: Long): Mono<ResponseEntity<List<Url>>> =
        pathAuthorization
            .clubAuthorization(clubId)
            .flatMap {
                LOGGER.info("updateUrls: cid: $clubId")
                urlService.saveUrl(
                    clubId,
                    dto.urls.map { url -> Url(url.urlid, clubId, url.name, url.color, url.url) },
                )
            }
            .invalidateStamp { ResponseStamp.URL.withKey("$clubId") }
            .wrapError()
}
