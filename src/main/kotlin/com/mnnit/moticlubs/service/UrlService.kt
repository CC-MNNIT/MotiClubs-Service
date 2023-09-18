package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.Url
import com.mnnit.moticlubs.repository.UrlRepository
import com.mnnit.moticlubs.utils.storeCache
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UrlService(
    private val urlRepository: UrlRepository,
) {

    @CacheEvict("urls", key = "#cid")
    fun saveUrl(cid: Long, urls: List<Url>): Mono<List<Url>> = urlRepository
        .deleteAllByCid(cid)
        .then(
            urlRepository
                .saveAll(urls)
                .collectList(),
        )

    @Cacheable("urls", key = "#cid")
    fun getUrlsByCid(cid: Long): Mono<List<Url>> = urlRepository
        .findAllByCid(cid)
        .collectList()
        .storeCache()
}
