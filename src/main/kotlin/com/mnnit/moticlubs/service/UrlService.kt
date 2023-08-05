package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.UrlRepository
import com.mnnit.moticlubs.dto.Url
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UrlService(
    private val urlRepository: UrlRepository,
) {

    fun saveUrl(cid: Long, urls: List<Url>): Mono<List<Url>> = urlRepository
        .findAllByCid(cid)
        .flatMap { deleteUrl(it) }
        .then(
            urlRepository
                .saveAll(urls)
                .collectList()
        )

    fun getUrlsByCid(cid: Long): Mono<List<Url>> = urlRepository
        .findAllByCid(cid)
        .collectList()

    fun deleteUrl(url: Url): Mono<Void> = urlRepository.delete(url)
}
