package com.mnnit.moticlubs.service

import com.google.firebase.auth.FirebaseAuth
import com.mnnit.moticlubs.dao.FCM
import com.mnnit.moticlubs.dao.User
import com.mnnit.moticlubs.dto.response.AdminUserDTO
import com.mnnit.moticlubs.repository.AdminRepository
import com.mnnit.moticlubs.repository.FCMRepository
import com.mnnit.moticlubs.repository.UserRepository
import com.mnnit.moticlubs.utils.Constants.USER_ID_CLAIM
import com.mnnit.moticlubs.utils.storeCache
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(
    private val adminRepository: AdminRepository,
    private val userRepository: UserRepository,
    private val fcmRepository: FCMRepository,
    private val firebaseAuth: FirebaseAuth,
) {

    @CacheEvict("user", allEntries = true)
    fun saveUser(user: User): Mono<User> = userRepository.save(user)
        .flatMap { savedUser ->
            fcmRepository.save(FCM(savedUser.uid, ""))
                .flatMap {
                    val record = firebaseAuth.getUserByEmail(savedUser.email)
                    firebaseAuth.setCustomUserClaims(
                        record.uid,
                        HashMap<String, Any>().apply {
                            this[USER_ID_CLAIM] = savedUser.uid
                        },
                    )
                    Mono.just(savedUser)
                }
        }

    @Cacheable("all_users")
    fun getAllUsers(): Mono<List<User>> = userRepository
        .findAll()
        .collectList()
        .storeCache()

    @Cacheable("user", key = "#uid")
    fun getUserByUid(uid: Long): Mono<User> = userRepository.findById(uid).storeCache()

    @Cacheable("admins")
    fun getAllAdminUsers(): Mono<List<AdminUserDTO>> = adminRepository
        .findAllAdmins()
        .flatMap { user ->
            adminRepository.findAllByUid(user.uid).map { AdminUserDTO(it, user) }
        }
        .collectList()

    @CacheEvict(cacheNames = ["user", "admins", "all_users", "members"], allEntries = true)
    fun updateAvatar(uid: Long, avatar: String): Mono<User> = userRepository.updateAvatar(uid, avatar)

    @CacheEvict(cacheNames = ["user", "admins", "all_users", "members"], allEntries = true)
    fun updateContact(uid: Long, contact: String): Mono<User> = userRepository.updateContact(uid, contact)
}
