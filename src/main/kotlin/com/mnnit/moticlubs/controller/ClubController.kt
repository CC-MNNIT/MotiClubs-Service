package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.Club
import com.mnnit.moticlubs.dao.Url
import com.mnnit.moticlubs.dto.request.SaveUrlsDTO
import com.mnnit.moticlubs.dto.request.UpdateClubDTO
import com.mnnit.moticlubs.service.ClubService
import com.mnnit.moticlubs.service.UrlService
import com.mnnit.moticlubs.utils.Constants
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.CLUBS_ROUTE
import com.mnnit.moticlubs.utils.Constants.CLUB_ID_CLAIM
import com.mnnit.moticlubs.utils.Constants.URL_ROUTE
import com.mnnit.moticlubs.utils.ResponseStamp
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.apiWrapper
import com.mnnit.moticlubs.utils.invalidateStamp
import com.mnnit.moticlubs.utils.validateRequestBody
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
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
@RequestMapping("/$BASE_PATH/$CLUBS_ROUTE")
@Tag(name = "ClubRoute")
class ClubController(
    private val pathAuthorization: PathAuthorization,
    private val clubService: ClubService,
    private val urlService: UrlService,
) {

    companion object {
        private val LOGGER = ServiceLogger.getLogger(ClubController::class.java)
    }

    @GetMapping
    @Operation(summary = "Get list of all the clubs")
    fun getClubs(
        @RequestHeader(Constants.STAMP_HEADER) stamp: Long,
    ): Mono<ResponseEntity<List<Club>>> = apiWrapper(
        key = ResponseStamp.CLUB,
        stampValue = stamp,
        authorization = pathAuthorization::userAuthorization,
        serviceCall = {
            LOGGER.info("getClubs")
            clubService.getAllClubs()
        },
    )

    @PutMapping("/{$CLUB_ID_CLAIM}")
    @Operation(summary = "Updates club avatar, description and summary")
    fun updateClub(
        @RequestBody dto: UpdateClubDTO,
        @PathVariable clubId: Long,
    ): Mono<ResponseEntity<Club>> = pathAuthorization
        .clubAuthorization(clubId)
        .validateRequestBody(dto)
        .flatMap {
            LOGGER.info("updateClub: cid: $clubId")
            clubService.updateClub(clubId, dto)
        }
        .invalidateStamp { ResponseStamp.CLUB }
        .wrapError()

    @GetMapping("/$URL_ROUTE")
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

    @PostMapping("/$URL_ROUTE")
    @Operation(summary = "Updates the list of urls for the club")
    fun updateUrls(
        @RequestBody dto: SaveUrlsDTO,
        @RequestParam clubId: Long,
    ): Mono<ResponseEntity<List<Url>>> = pathAuthorization
        .clubAuthorization(clubId)
        .validateRequestBody(dto)
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
