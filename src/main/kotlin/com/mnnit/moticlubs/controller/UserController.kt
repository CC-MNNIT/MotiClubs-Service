package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dto.FCM
import com.mnnit.moticlubs.dto.Subscriber
import com.mnnit.moticlubs.dto.User
import com.mnnit.moticlubs.dto.request.UpdateAvatarDTO
import com.mnnit.moticlubs.service.FCMService
import com.mnnit.moticlubs.service.SubscriberService
import com.mnnit.moticlubs.service.UserService
import com.mnnit.moticlubs.utils.Constants.BASE_PATH
import com.mnnit.moticlubs.utils.Constants.USER_ID_CLAIM
import com.mnnit.moticlubs.utils.Constants.USER_ROUTE
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

    @GetMapping
    @Operation(summary = "Returns details yourself")
    fun getSelf(): Mono<User> = pathAuthorization
        .userAuthorization()
        .flatMap { userService.getUserByUid(it) }
        .wrapError()

    @GetMapping("/{$USER_ID_CLAIM}")
    @Operation(summary = "Returns details of requested user")
    fun getUser(@PathVariable userId: Long): Mono<User> = pathAuthorization
        .userAuthorization()
        .flatMap { userService.getUserByUid(userId) }
        .wrapError()

    @GetMapping("/admins")
    @Operation(summary = "Returns list of details of all the users who are admin")
    fun getAdmins(): Mono<List<User>> = pathAuthorization
        .userAuthorization()
        .flatMap { userService.getAllAdminUsers() }
        .wrapError()

    @PostMapping
    @Operation(summary = "Saves user")
    fun saveUser(@RequestBody user: User): Mono<User> = pathAuthorization
        .userAuthorization()
        .flatMap { userService.saveUser(user) }
        .wrapError()

    @PostMapping("/avatar")
    @Operation(summary = "Updates user avatar")
    fun updateAvatar(@RequestBody dto: UpdateAvatarDTO): Mono<User> = pathAuthorization
        .userAuthorization()
        .flatMap { userService.updateAvatar(it, dto.avatar) }
        .wrapError()

    @PostMapping("/fcm")
    @Operation(summary = "Update fcm token for the user")
    fun updateFCM(@RequestBody fcm: FCM): Mono<FCM> = pathAuthorization
        .userAuthorization()
        .flatMap { fcmService.updateFcm(fcm) }
        .wrapError()

    @PostMapping("/subscribe")
    @Operation(summary = "Subscribe a club")
    fun subscribeClub(@RequestBody subscriber: Subscriber): Mono<Subscriber> = pathAuthorization
        .userAuthorization()
        .flatMap { subscriberService.subscribe(subscriber) }
        .wrapError()

    @PostMapping("/unsubscribe")
    @Operation(summary = "Unsubscribe a club")
    fun unsubscribeClub(@RequestBody subscriber: Subscriber): Mono<Void> = pathAuthorization
        .userAuthorization()
        .flatMap { subscriberService.unsubscribe(subscriber) }
        .wrapError()
}
