package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.View
import com.mnnit.moticlubs.repository.ViewRepository
import com.mnnit.moticlubs.utils.storeCache
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ViewService(
    private val viewRepository: ViewRepository,
) {

    @CacheEvict("views", allEntries = true)
    fun saveView(view: View): Mono<View> = viewRepository.save(view)

    @Cacheable("views", key = "#pid")
    fun getViewsByPid(pid: Long): Mono<List<View>> = viewRepository
        .findAllByPid(pid)
        .collectList()
        .storeCache()
}
