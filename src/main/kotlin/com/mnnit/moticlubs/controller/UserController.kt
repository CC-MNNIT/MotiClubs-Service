package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.FCM
import com.mnnit.moticlubs.dao.User
import com.mnnit.moticlubs.dto.request.FCMTokenDTO
import com.mnnit.moticlubs.dto.request.UpdateAvatarDTO
import com.mnnit.moticlubs.dto.response.AdminUserDTO
import com.mnnit.moticlubs.service.FCMService
import com.mnnit.moticlubs.service.UserService
import com.mnnit.moticlubs.utils.*
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.STAMP_HEADER
import com.mnnit.moticlubs.utils.Constants.USER_ID_CLAIM
import com.mnnit.moticlubs.utils.Constants.USER_ROUTE
import com.mnnit.moticlubs.utils.ResponseStamp.invalidateStamp
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/$BASE_PATH/$USER_ROUTE")
@Tag(name = "UserRoute")
class UserController(
    private val pathAuthorization: PathAuthorization,
    private val userService: UserService,
    private val fcmService: FCMService,
) {

    companion object {
        private val LOGGER = ServiceLogger.getLogger(UserController::class.java)
    }

    @GetMapping
    @Operation(summary = "Returns details yourself")
    fun getSelf(
        @RequestHeader(STAMP_HEADER) stamp: Long,
        serverRequest: ServerHttpRequest,
    ): Mono<ResponseEntity<User>> = apiWrapper(
        key = ResponseStamp.USER,
        stampValue = stamp,
        authorization = pathAuthorization::userAuthorization,
        serviceCall = {
            LOGGER.info("getSelf: uid: $it, ${serverRequest.path.value()}")
            userService.getUserByUid(it)
        }
    )

    @GetMapping("/{$USER_ID_CLAIM}")
    @Operation(summary = "Returns details of requested user")
    fun getUser(
        @PathVariable userId: Long,
        @RequestHeader(STAMP_HEADER) stamp: Long,
    ): Mono<ResponseEntity<User>> = apiWrapper(
        key = ResponseStamp.USER,
        stampValue = stamp,
        authorization = pathAuthorization::userAuthorization,
        serviceCall = {
            LOGGER.info("getUser: uid: $userId")
            userService.getUserByUid(userId)
        }
    )

    @GetMapping("/all")
    @Operation(summary = "Returns list of all the users")
    fun getAllUsers(
        @RequestHeader(STAMP_HEADER) stamp: Long,
    ): Mono<ResponseEntity<List<User>>> = apiWrapper(
        key = ResponseStamp.USER,
        stampValue = stamp,
        authorization = pathAuthorization::userAuthorization,
        serviceCall = {
            LOGGER.info("getAll")
            userService.getAllUsers()
        }
    )

    @GetMapping("/admins")
    @Operation(summary = "Returns list of details of all the users who are admin")
    fun getAdmins(
        @RequestHeader(STAMP_HEADER) stamp: Long,
    ): Mono<ResponseEntity<List<AdminUserDTO>>> = apiWrapper(
        key = ResponseStamp.ADMIN,
        stampValue = stamp,
        authorization = pathAuthorization::userAuthorization,
        serviceCall = {
            LOGGER.info("getAdmins")
            userService.getAllAdminUsers()
        }
    )

    @PostMapping
    @Operation(summary = "Saves user")
    fun saveUser(@RequestBody user: User): Mono<ResponseEntity<User>> {
        LOGGER.info("saveUser: user: ${user.regno}")
        return userService.saveUser(user)
            .invalidateStamp { ResponseStamp.USER }
            .wrapError()
    }

    @PostMapping("/avatar")
    @Operation(summary = "Updates user avatar")
    fun updateAvatar(@RequestBody dto: UpdateAvatarDTO): Mono<ResponseEntity<User>> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("updateAvatar")
            userService.updateAvatar(it, dto.avatar)
        }
        .invalidateStamp {
            ResponseStamp.ADMIN.invalidateStamp()
            ResponseStamp.USER
        }
        .wrapError()

    @PostMapping("/fcm")
    @Operation(summary = "Update fcm token for the user")
    fun updateFCM(@RequestBody dto: FCMTokenDTO): Mono<FCM> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("updateFCM: uid: $it")
            fcmService.updateFcm(FCM(it, dto.token))
        }
        .wrapError()
}
