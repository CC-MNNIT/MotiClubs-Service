package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.Url
import com.mnnit.moticlubs.repository.UrlRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UrlService(
    private val urlRepository: UrlRepository,
) {

    fun saveUrl(cid: Long, urls: List<Url>): Mono<List<Url>> = urlRepository
        .deleteAllByCid(cid)
        .then(
            urlRepository
                .saveAll(urls)
                .collectList()
        )

    fun getUrlsByCid(cid: Long): Mono<List<Url>> = urlRepository
        .findAllByCid(cid)
        .collectList()
}
