package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.Admin
import com.mnnit.moticlubs.dao.Channel
import com.mnnit.moticlubs.dao.Club
import com.mnnit.moticlubs.dto.request.AddClubDTO
import com.mnnit.moticlubs.dto.request.AssignAdminDTO
import com.mnnit.moticlubs.service.AdminService
import com.mnnit.moticlubs.service.ChannelService
import com.mnnit.moticlubs.service.ClubService
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.SUPER_ADMIN_ROUTE
import com.mnnit.moticlubs.utils.ResponseStamp
import com.mnnit.moticlubs.utils.ResponseStamp.invalidateStamp
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.invalidateStamp
import com.mnnit.moticlubs.utils.validateRequestBody
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/$BASE_PATH/$SUPER_ADMIN_ROUTE")
@Tag(name = "SuperAdminRoute")
class SuperAdminController(
    private val pathAuthorization: PathAuthorization,
    private val clubService: ClubService,
    private val channelService: ChannelService,
    private val adminService: AdminService,
) {

    companion object {
        private val LOGGER = ServiceLogger.getLogger(SuperAdminController::class.java)
    }

    @GetMapping("/login")
    @Operation(summary = "Check if you are super admin")
    fun login(): Mono<String> = pathAuthorization
        .superAdminAuthorization()
        .flatMap {
            LOGGER.info("super-admin logged in")
            Mono.just("super-admin logged in")
        }
        .wrapError()

    @PostMapping("/add_club")
    @Operation(summary = "Adds a new club")
    fun addClub(@RequestBody dto: AddClubDTO): Mono<ResponseEntity<Club>> = pathAuthorization
        .superAdminAuthorization()
        .validateRequestBody(dto)
        .flatMap {
            LOGGER.info("addClub: dto: $dto")
            clubService.saveClub(
                Club(
                    name = dto.name,
                    description = dto.description,
                    summary = dto.summary,
                ),
            )
        }
        .flatMap { club ->
            LOGGER.info("addClub: creating general channel")
            channelService.saveChannel(
                Channel(cid = club.cid, name = "General", private = false),
            ).map { club }
        }
        .invalidateStamp {
            ResponseStamp.CHANNEL.invalidateStamp()
            ResponseStamp.CLUB
        }
        .wrapError()

    @DeleteMapping("/delete_club")
    @Operation(summary = "Deletes club")
    fun deleteClub(@RequestParam clubId: Long): Mono<ResponseEntity<Void>> = pathAuthorization
        .superAdminAuthorization()
        .flatMap {
            LOGGER.info("deleteClubByCid: cid: $clubId")
            clubService.deleteClubByCid(clubId)
        }
        .invalidateStamp {
            ResponseStamp.CHANNEL.invalidateStamp()
            ResponseStamp.CLUB
        }
        .wrapError()

    @PostMapping("/add_admin")
    @Operation(summary = "Makes user admin of club")
    fun assignAdmin(@RequestBody dto: AssignAdminDTO): Mono<ResponseEntity<Admin>> = pathAuthorization
        .superAdminAuthorization()
        .validateRequestBody(dto)
        .flatMap {
            LOGGER.info("assignAdmin: dto: $dto")
            adminService.saveAdmin(dto)
        }
        .invalidateStamp {
            ResponseStamp.CHANNEL.invalidateStamp()
            ResponseStamp.MEMBER.invalidateStamp()
            ResponseStamp.ADMIN
        }
        .wrapError()

    @PostMapping("/remove_admin")
    @Operation(summary = "Remove user from club admin")
    fun removeAdmin(@RequestBody dto: AssignAdminDTO): Mono<ResponseEntity<Void>> = pathAuthorization
        .superAdminAuthorization()
        .validateRequestBody(dto)
        .flatMap {
            LOGGER.info("removeAdmin: dto: $dto")
            adminService.removeAdmin(dto)
        }
        .invalidateStamp {
            ResponseStamp.CHANNEL.invalidateStamp()
            ResponseStamp.MEMBER.invalidateStamp()
            ResponseStamp.ADMIN
        }
        .wrapError()

    @PostMapping("/invalidate_stamps")
    @Operation(summary = "Invalidate all variants of stamps")
    fun invalidateStamp(): Mono<ResponseEntity<Void>> = pathAuthorization
        .superAdminAuthorization()
        .flatMap { Mono.just(it).then() }
        .invalidateStamp {
            ResponseStamp.NONE.invalidateStamp()
            ResponseStamp.ADMIN.invalidateStamp()
            ResponseStamp.CHANNEL.invalidateStamp()
            ResponseStamp.CLUB.invalidateStamp()
            ResponseStamp.MEMBER.invalidateStamp()
            ResponseStamp.POST.invalidateStamp()
            ResponseStamp.REPLY.invalidateStamp()
            ResponseStamp.URL.invalidateStamp()
            ResponseStamp.USER
        }
        .wrapError()
}
