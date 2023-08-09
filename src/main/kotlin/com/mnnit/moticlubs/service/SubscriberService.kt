package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.Subscribers
import com.mnnit.moticlubs.repository.SubscriberRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class SubscriberService(
    private val subscriberRepository: SubscriberRepository,
) {

    fun subscribe(subscribers: Subscribers): Mono<Subscribers> = subscriberRepository.save(subscribers)

    fun unsubscribe(subscribers: Subscribers): Mono<Void> = subscriberRepository.delete(subscribers)

    fun getSubscribersByCid(cid: Long): Mono<List<Subscribers>> = subscriberRepository
        .findAllByCid(cid)
        .collectList()

    fun getSubscribedClubsByUid(uid: Long): Mono<List<Subscribers>> = subscriberRepository
        .findAllByUid(uid)
        .collectList()
}
