package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.SubscriberRepository
import com.mnnit.moticlubs.dto.Subscriber
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class SubscriberService(
    private val subscriberRepository: SubscriberRepository,
) {

    fun subscribe(subscriber: Subscriber): Mono<Subscriber> = subscriberRepository.save(subscriber)

    fun unsubscribe(subscriber: Subscriber): Mono<Void> = subscriberRepository.delete(subscriber)

    fun getSubscribersByCid(cid: Long): Mono<List<Subscriber>> = subscriberRepository
        .findAllByCid(cid)
        .collectList()

    fun getSubscribedClubsByUid(uid: Long): Mono<List<Subscriber>> = subscriberRepository
        .findAllById(arrayListOf(uid))
        .collectList()
}
