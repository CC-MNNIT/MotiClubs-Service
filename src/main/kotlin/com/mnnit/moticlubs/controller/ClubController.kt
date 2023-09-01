package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.Club
import com.mnnit.moticlubs.dto.request.UpdateClubDTO
import com.mnnit.moticlubs.service.ClubService
import com.mnnit.moticlubs.utils.*
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.CLUBS_ROUTE
import com.mnnit.moticlubs.utils.Constants.CLUB_ID_CLAIM
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/$BASE_PATH/$CLUBS_ROUTE")
@Tag(name = "ClubRoute")
class ClubController(
    private val pathAuthorization: PathAuthorization,
    private val clubService: ClubService,
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
        }
    )

    @PutMapping("/{$CLUB_ID_CLAIM}")
    @Operation(summary = "Updates club avatar, description and summary")
    fun updateClub(
        @RequestBody dto: UpdateClubDTO,
        @PathVariable clubId: Long
    ): Mono<ResponseEntity<Club>> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap {
            LOGGER.info("updateClub: cid: $clubId")
            clubService.updateClub(clubId, dto)
        }
        .invalidateStamp { ResponseStamp.CLUB }
        .wrapError()
}
