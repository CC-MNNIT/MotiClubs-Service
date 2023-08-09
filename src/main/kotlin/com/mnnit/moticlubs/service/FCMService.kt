package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.FCM
import com.mnnit.moticlubs.repository.FCMRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class FCMService(
    private val fcmRepository: FCMRepository,
) {

    fun updateFcm(fcm: FCM): Mono<FCM> = fcmRepository.update(fcm)
}
