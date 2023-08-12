package com.mnnit.moticlubs.service

import com.google.firebase.auth.FirebaseAuth
import com.mnnit.moticlubs.dao.FCM
import com.mnnit.moticlubs.dao.User
import com.mnnit.moticlubs.dto.response.AdminUserDTO
import com.mnnit.moticlubs.repository.AdminRepository
import com.mnnit.moticlubs.repository.FCMRepository
import com.mnnit.moticlubs.repository.UserRepository
import com.mnnit.moticlubs.utils.Constants.USER_ID_CLAIM
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(
    private val adminRepository: AdminRepository,
    private val userRepository: UserRepository,
    private val fcmRepository: FCMRepository,
    private val firebaseAuth: FirebaseAuth,
) {

    fun saveUser(user: User): Mono<User> = userRepository.save(user)
        .flatMap { savedUser ->
            fcmRepository.save(FCM(savedUser.uid, ""))
                .flatMap {
                    val record = firebaseAuth.getUserByEmail(user.email)
                    firebaseAuth.setCustomUserClaims(record.uid, HashMap<String, Any>().apply {
                        this[USER_ID_CLAIM] = user.uid
                    })
                    Mono.just(savedUser)
                }
        }

    fun getUserByUid(uid: Long): Mono<User> = userRepository.findById(uid)

    fun getUserByRegNo(regNo: String): Mono<User> = userRepository.findByRegNo(regNo)

    fun getAllAdminUsers(): Mono<List<AdminUserDTO>> = adminRepository
        .findAll()
        .flatMap { admin -> getUserByUid(admin.uid).flatMap { user -> Mono.just(AdminUserDTO(admin.cid, user)) } }
        .collectList()

    fun updateAvatar(uid: Long, avatar: String): Mono<User> = userRepository
        .findById(uid)
        .flatMap { saveUser(it.copy(avatar = avatar)) }

    fun deleteUser(uid: Long) = userRepository.deleteById(uid)
}
