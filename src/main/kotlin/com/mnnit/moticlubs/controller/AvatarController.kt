package com.mnnit.moticlubs.controller

import com.mnnit.moticlubs.dao.Club
import com.mnnit.moticlubs.dao.User
import com.mnnit.moticlubs.dto.response.ImageUrlDTO
import com.mnnit.moticlubs.service.ClubService
import com.mnnit.moticlubs.service.UserService
import com.mnnit.moticlubs.utils.BadRequestException
import com.mnnit.moticlubs.utils.Constants
import com.mnnit.moticlubs.utils.Constants.CLUB_AVATAR_PATH
import com.mnnit.moticlubs.utils.Constants.POST_AVATAR_PATH
import com.mnnit.moticlubs.utils.Constants.USER_AVATAR_PATH
import com.mnnit.moticlubs.utils.NotFoundException
import com.mnnit.moticlubs.utils.ResponseStamp
import com.mnnit.moticlubs.utils.ResponseStamp.invalidateStamp
import com.mnnit.moticlubs.utils.ServiceLogger
import com.mnnit.moticlubs.utils.invalidateStamp
import com.mnnit.moticlubs.utils.wrapError
import com.mnnit.moticlubs.web.security.PathAuthorization
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.env.Environment
import org.springframework.core.io.UrlResource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.CacheControl
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.util.function.Tuple2
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.time.Duration
import java.util.*

@RestController
@RequestMapping("/${Constants.BASE_PATH}/${Constants.AVATAR_ROUTE}")
@Tag(name = "AvatarRoute")
class AvatarController(
    private val environment: Environment,
    private val pathAuthorization: PathAuthorization,
    private val clubService: ClubService,
    private val userService: UserService,
) {

    private enum class ImageType {
        USER, CLUB, POST
    }

    companion object {
        private val LOGGER = ServiceLogger.getLogger(AvatarController::class.java)
    }

    // -------------------- GET AVATARS

    @GetMapping("/g/user/{hash}", produces = ["image/webp"])
    @Operation(summary = "Get avatar of user")
    fun getUserAvatar(
        @PathVariable hash: String,
    ): Mono<ResponseEntity<Flux<DataBuffer>>> = getImage(ImageType.USER, hash)

    @GetMapping("/g/club/{hash}", produces = ["image/webp"])
    @Operation(summary = "Get avatar of club")
    fun getClubAvatar(
        @PathVariable hash: String,
    ): Mono<ResponseEntity<Flux<DataBuffer>>> = getImage(ImageType.CLUB, hash)

    @GetMapping("/g/post/{hash}", produces = ["image/webp"])
    @Operation(summary = "Get image for post")
    fun getPostImage(
        @PathVariable hash: String,
    ): Mono<ResponseEntity<Flux<DataBuffer>>> = getImage(ImageType.POST, hash)

    private fun getImage(imageType: ImageType, encodedId: String): Mono<ResponseEntity<Flux<DataBuffer>>> = Mono
        .fromCallable {
            val id = encodedId.decodeToId()
            LOGGER.info("getImage: ${imageType.name} for id: $id")
            UrlResource(
                Paths
                    .get(
                        when (imageType) {
                            ImageType.USER -> USER_AVATAR_PATH
                            ImageType.CLUB -> CLUB_AVATAR_PATH
                            ImageType.POST -> POST_AVATAR_PATH
                        },
                    )
                    .resolve(id.getFileName())
                    .toUri(),
            )
        }
        .flatMap { resource ->
            if (resource.exists() && resource.isReadable) {
                LOGGER.info("getImage: found")
                Mono.just(DataBufferUtils.read(resource, DefaultDataBufferFactory(), 4096))
            } else {
                Mono.error(NotFoundException("Image for ${imageType.name} doesn't exists"))
            }
        }
        .flatMap {
            Mono.just(
                ResponseEntity.ok()
                    .eTag(encodedId)
                    .cacheControl(CacheControl.maxAge(Duration.ZERO).cachePublic())
                    .body(it),
            )
        }
        .wrapError()

    // -------------------- UPDATE AVATARS

    @PostMapping("/user", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "Updates user avatar")
    @Hidden
    fun updateUserAvatar(
        @RequestPart("file") filePart: Mono<FilePart>,
        serverRequest: ServerHttpRequest,
    ): Mono<ResponseEntity<User>> = pathAuthorization
        .userAuthorization()
        .zipWith(filePart)
        .saveImage(serverRequest, ImageType.USER) { uid, url -> userService.updateAvatar(uid, url) }
        .invalidateStamp {
            ResponseStamp.ADMIN.invalidateStamp()
            ResponseStamp.USER.withKey("${it.uid}")
        }
        .wrapError()

    @PostMapping("/club", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "Updates club avatar")
    @Hidden
    fun updateClubAvatar(
        @RequestPart("file") filePart: Mono<FilePart>,
        @RequestParam clubId: Long,
        serverRequest: ServerHttpRequest,
    ): Mono<ResponseEntity<Club>> = pathAuthorization
        .clubAuthorization(clubId)
        .map { clubId }
        .zipWith(filePart)
        .saveImage(serverRequest, ImageType.CLUB) { cid, url -> clubService.updateAvatar(cid, url) }
        .invalidateStamp { ResponseStamp.CLUB }
        .wrapError()

    @PostMapping("/post", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "Uploads image for posts")
    @Hidden
    fun addPostImage(
        @RequestPart("file") filePart: Mono<FilePart>,
        @RequestParam clubId: Long,
        serverRequest: ServerHttpRequest,
    ): Mono<ResponseEntity<ImageUrlDTO>> = pathAuthorization
        .clubAuthorization(clubId)
        .map { System.currentTimeMillis() }
        .zipWith(filePart)
        .saveImage(serverRequest, ImageType.POST) { _, url -> Mono.just(ImageUrlDTO(url)) }
        .invalidateStamp { ResponseStamp.NONE }
        .wrapError()

    private fun <T> Mono<Tuple2<Long, FilePart>>.saveImage(
        serverRequest: ServerHttpRequest,
        imageType: ImageType,
        then: (Long, String) -> Mono<T>,
    ): Mono<T> = flatMap { tuple ->
        val id = tuple.t1
        val fp = tuple.t2

        val validation = validateFile(serverRequest, fp)
        if (validation != null) {
            return@flatMap Mono.error(BadRequestException("Invalid file: $validation"))
        }

        val fileName = id.getFileName()
        val rawUrl = if (environment.activeProfiles.contains("prod")) {
            "https://sac.mnnit.ac.in/moticlubs/api/v1/avatar"
        } else {
            "http://localhost:8002/api/v1/avatar"
        }

        val hashPrefix = id.encodeToUrl()
        val (path, url) = when (imageType) {
            ImageType.USER -> Pair(USER_AVATAR_PATH, "$rawUrl/g/user/$hashPrefix")
            ImageType.CLUB -> Pair(CLUB_AVATAR_PATH, "$rawUrl/g/club/$hashPrefix")
            ImageType.POST -> Pair(POST_AVATAR_PATH, "$rawUrl/g/post/$hashPrefix")
        }

        val folder = File(path)
        if (!folder.exists() && !folder.mkdirs()) {
            return@flatMap Mono.error(BadRequestException("Unable to save file"))
        }

        LOGGER.info("saveImage: URL: $url")
        fp.transferTo(Paths.get("$path/$fileName"))
            .then(then(id, url))
    }.switchIfEmpty { Mono.error(RuntimeException("Unable to save file")) }

    private fun validateFile(serverRequest: ServerHttpRequest, filePart: FilePart): String? {
        // If it's a file, it will have CONTENT_LENGTH header
        val length = serverRequest.headers[HttpHeaders.CONTENT_LENGTH]?.first()?.toLong()
        length ?: return "No length"

        // File size <= 200KB
        if (length > (200 * 1024).toLong()) {
            return "File too large"
        }

        // File must be an image
        val name = filePart.filename()
        if (!name.endsWith(".png", true) &&
            !name.endsWith(".jpg", true) &&
            !name.endsWith(".jpeg", true) &&
            !name.endsWith(".webp", true)
        ) {
            return "Wrong format"
        }
        return null
    }

    private fun Long.getFileName(): String = "$this.webp"

    private fun Long.encodeToUrl(): String = Base64
        .getEncoder()
        .encode("$this.${System.currentTimeMillis()}".toByteArray()).toString(StandardCharsets.UTF_8)

    private fun String.decodeToId(): Long = Base64
        .getDecoder()
        .decode(this)
        .toString(StandardCharsets.UTF_8)
        .split(".")[0]
        .toLongOrNull() ?: 0
}
