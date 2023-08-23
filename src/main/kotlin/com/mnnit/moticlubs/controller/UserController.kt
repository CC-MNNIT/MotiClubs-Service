package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.FCM
import com.mnnit.moticlubs.dao.Subscribers
import com.mnnit.moticlubs.dao.User
import com.mnnit.moticlubs.dto.request.FCMTokenDTO
import com.mnnit.moticlubs.dto.request.SubscriberDto
import com.mnnit.moticlubs.dto.request.UpdateAvatarDTO
import com.mnnit.moticlubs.dto.response.AdminUserDTO
import com.mnnit.moticlubs.service.FCMService
import com.mnnit.moticlubs.service.SubscriberService
import com.mnnit.moticlubs.service.UserService
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.USER_ID_CLAIM
import com.mnnit.moticlubs.utils.Constants.USER_ROUTE
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/$BASE_PATH/$USER_ROUTE")
@Tag(name = "UserRoute")
class UserController(
    private val pathAuthorization: PathAuthorization,
    private val userService: UserService,
    private val fcmService: FCMService,
    private val subscriberService: SubscriberService,
) {

    companion object {
        private val LOGGER = ServiceLogger.getLogger(UserController::class.java)
    }

    @GetMapping
    @Operation(summary = "Returns details yourself")
    fun getSelf(): Mono<User> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("getSelf: uid: $it")
            userService.getUserByUid(it)
        }
        .wrapError()

    @GetMapping("/{$USER_ID_CLAIM}")
    @Operation(summary = "Returns details of requested user")
    fun getUser(@PathVariable userId: Long): Mono<User> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("getUser: uid: $userId")
            userService.getUserByUid(userId)
        }
        .wrapError()

    @GetMapping("/admins")
    @Operation(summary = "Returns list of details of all the users who are admin")
    fun getAdmins(): Mono<List<AdminUserDTO>> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("getAdmins")
            userService.getAllAdminUsers()
        }
        .wrapError()

    @PostMapping
    @Operation(summary = "Saves user")
    fun saveUser(@RequestBody user: User): Mono<User> {
        LOGGER.info("saveUser: user: ${user.regno}")
        return userService.saveUser(user).wrapError()
    }

    @PostMapping("/avatar")
    @Operation(summary = "Updates user avatar")
    fun updateAvatar(@RequestBody dto: UpdateAvatarDTO): Mono<User> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("updateAvatar")
            userService.updateAvatar(it, dto.avatar)
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

    @PostMapping("/subscribe")
    @Operation(summary = "Subscribe a club")
    fun subscribeClub(@RequestBody dto: SubscriberDto): Mono<Subscribers> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("subscribeClub: dto: $dto")
            subscriberService.subscribe(Subscribers(dto.clubId, it))
        }
        .wrapError()

    @PostMapping("/unsubscribe")
    @Operation(summary = "Unsubscribe a club")
    fun unsubscribeClub(@RequestBody dto: SubscriberDto): Mono<Void> = pathAuthorization
        .userAuthorization()
        .flatMap {
            LOGGER.info("unsubscribeClub: dto: $dto")
            subscriberService.unsubscribe(Subscribers(dto.clubId, it))
        }
        .wrapError()
}
