package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.Admin
import com.mnnit.moticlubs.dao.Club
import com.mnnit.moticlubs.dto.request.AssignAdminDTO
import com.mnnit.moticlubs.service.AdminService
import com.mnnit.moticlubs.service.ClubService
import com.mnnit.moticlubs.service.UserService
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.SUPER_ADMIN_ROUTE
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/$BASE_PATH/$SUPER_ADMIN_ROUTE")
@Tag(name = "SuperAdminRoute")
class SuperAdminController(
    private val pathAuthorization: PathAuthorization,
    private val clubService: ClubService,
    private val userService: UserService,
    private val adminService: AdminService,
) {

    companion object {
        private val LOGGER = ServiceLogger.getLogger(SuperAdminController::class.java)
    }

    @GetMapping("/login")
    @Operation(summary = "Logs user in")
    fun login(): Mono<String> = pathAuthorization
        .superAdminAuthorization()
        .flatMap {
            LOGGER.info("login")
            Mono.just("Logged in")
        }
        .wrapError()

    @PostMapping("/add_club")
    @Operation(summary = "Adds a new club")
    fun addClub(@RequestBody club: Club): Mono<Club> = pathAuthorization
        .superAdminAuthorization()
        .flatMap {
            LOGGER.info("addClub: club: ${club.name}")
            clubService.saveClub(club)
        }
        .wrapError()

    @DeleteMapping("/delete_club")
    @Operation(summary = "Deletes club")
    fun deleteClub(@RequestParam clubId: Long): Mono<Void> = pathAuthorization
        .superAdminAuthorization()
        .flatMap {
            LOGGER.info("deleteClubByCid: cid: $clubId")
            clubService.deleteClubByCid(clubId)
        }
        .wrapError()

    @PostMapping("/add_admin")
    @Operation(summary = "Makes user admin of club")
    fun assignAdmin(@RequestBody dto: AssignAdminDTO): Mono<Admin> = pathAuthorization
        .superAdminAuthorization()
        .flatMap {
            LOGGER.info("assignAdmin: dto: $dto")
            userService.getUserByEmail(dto.email)
                .flatMap { user -> adminService.saveAdmin(Admin(dto.cid, user.uid)) }
        }
        .wrapError()

    @PostMapping("/remove_admin")
    @Operation(summary = "Remove user from club admin")
    fun removeAdmin(@RequestBody admin: Admin): Mono<Void> = pathAuthorization
        .superAdminAuthorization()
        .flatMap {
            LOGGER.info("removeAdmin: admin: $admin")
            adminService.removeAdmin(admin)
        }
        .wrapError()
}
