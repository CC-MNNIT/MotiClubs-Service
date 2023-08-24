package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.Club
import com.mnnit.moticlubs.dao.Member
import com.mnnit.moticlubs.dto.request.MembersDTO
import com.mnnit.moticlubs.dto.request.UpdateClubDTO
import com.mnnit.moticlubs.service.ClubService
import com.mnnit.moticlubs.service.MemberService
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.CLUBS_ROUTE
import com.mnnit.moticlubs.utils.Constants.CLUB_ID_CLAIM
import com.mnnit.moticlubs.utils.ServiceLogger
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
    private val memberService: MemberService,
) {

    companion object {
        private val LOGGER = ServiceLogger.getLogger(ClubController::class.java)
    }

    @GetMapping
    @Operation(summary = "Get list of all the clubs")
    fun getClubs(): Mono<List<Club>> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("getClubs")
            clubService.getAllClubs()
        }
        .wrapError()

    @GetMapping("/members/{$CLUB_ID_CLAIM}")
    @Operation(summary = "Returns list of userIds that are member of the clubId")
    fun getMembers(@PathVariable clubId: Long): Mono<List<Member>> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("getMembers: cid: $clubId")
            memberService.getMembersByCid(clubId)
        }
        .wrapError()

    @PostMapping("/members")
    @Operation(summary = "Makes list of userIds member of the clubId")
    fun addMembers(@RequestBody dto: MembersDTO): Mono<List<Member>> = pathAuthorization
        .clubAuthorization(dto.cid)
        .flatMap {
            LOGGER.info("addMembers: cid: ${dto.cid}; chid: ${dto.chid}")
            memberService.addMembers(dto)
        }
        .wrapError()

    @DeleteMapping("/members")
    @Operation(summary = "Removes list of userIds from the member of the clubId")
    fun removeMembers(@RequestBody dto: MembersDTO): Mono<Void> = pathAuthorization
        .clubAuthorization(dto.cid)
        .flatMap {
            LOGGER.info("removeMembers: cid: ${dto.cid}; chid: ${dto.chid}")
            memberService.removeMembers(dto)
        }
        .wrapError()

    @PutMapping("/{$CLUB_ID_CLAIM}")
    @Operation(summary = "Updates club avatar, description and summary")
    fun updateClub(
        @RequestBody dto: UpdateClubDTO,
        @PathVariable clubId: Long
    ): Mono<Club> = pathAuthorization
        .clubAuthorization(clubId)
        .flatMap {
            LOGGER.info("updateClub: cid: $clubId")
            clubService.updateClub(clubId, dto)
        }
        .wrapError()
}
