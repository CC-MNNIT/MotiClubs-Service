package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.FCMRepository
import com.mnnit.moticlubs.dto.FCM
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class FCMService(
    private val fcmRepository: FCMRepository,
) {

    fun saveFcm(fcm: FCM): Mono<FCM> = fcmRepository.save(fcm)

    fun updateFcm(fcm: FCM): Mono<FCM> = fcmRepository
        .findById(fcm.uid)
        .flatMap { saveFcm(it.copy(token = fcm.token)) }
}
