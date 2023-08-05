package com.mnnit.moticlubs.service

import com.google.firebase.auth.FirebaseAuth
import com.mnnit.moticlubs.dao.AdminRepository
import com.mnnit.moticlubs.dao.FCMRepository
import com.mnnit.moticlubs.dao.UserRepository
import com.mnnit.moticlubs.dto.FCM
import com.mnnit.moticlubs.dto.User
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

    fun getUserByEmail(email: String): Mono<User> = userRepository.findUserByEmail(email)

    fun getAllUsers(): Mono<List<User>> = userRepository
        .findAll()
        .collectList()

    fun getAllAdminUsers(): Mono<List<User>> = adminRepository
        .findAll()
        .flatMap { getUserByUid(it.uid) }
        .collectList()

    fun updateAvatar(uid: Long, avatar: String): Mono<User> = userRepository
        .findById(uid)
        .flatMap { saveUser(it.copy(avatar = avatar)) }

    fun deleteUser(uid: Long) = userRepository.deleteById(uid)
}
