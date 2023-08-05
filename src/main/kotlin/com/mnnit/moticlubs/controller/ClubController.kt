package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dto.Club
import com.mnnit.moticlubs.dto.Subscriber
import com.mnnit.moticlubs.dto.request.UpdateClubDTO
import com.mnnit.moticlubs.service.ClubService
import com.mnnit.moticlubs.service.SubscriberService
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.CLUBS_ROUTE
import com.mnnit.moticlubs.utils.Constants.CLUB_ID_CLAIM
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/$BASE_PATH/$CLUBS_ROUTE")
@Tag(name = "ClubRoute")
class ClubController(
    private val pathAuthorization: PathAuthorization,
    private val clubService: ClubService,
    private val subscriberService: SubscriberService,
) {

    @GetMapping
    @Operation(summary = "Get list of all the clubs")
    fun getClubs(): Mono<List<Club>> = pathAuthorization
        .userAuthorization()
        .flatMap { clubService.getAllClubs() }
        .wrapError()

    @GetMapping("/subscribers/{$CLUB_ID_CLAIM}")
    @Operation(summary = "Returns list of userIds subscribed to the clubId")
    fun getSubscribers(@PathVariable clubId: Long): Mono<List<Subscriber>> = pathAuthorization
        .userAuthorization()
        .flatMap { subscriberService.getSubscribersByCid(clubId) }
        .wrapError()

    @PutMapping("/{$CLUB_ID_CLAIM}")
    @Operation(summary = "Updates club avatar, description and summary")
    fun updateClub(
        @RequestBody dto: UpdateClubDTO,
        @PathVariable clubId: Long
    ): Mono<Club> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap { clubService.updateClub(clubId, dto) }
        .wrapError()
}
