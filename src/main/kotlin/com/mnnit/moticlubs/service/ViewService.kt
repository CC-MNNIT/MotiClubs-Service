package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.View
import com.mnnit.moticlubs.repository.ViewRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ViewService(
    private val viewRepository: ViewRepository,
) {

    fun saveView(view: View): Mono<View> = viewRepository.save(view)

    fun getViewsByPid(pid: Long): Mono<List<View>> = viewRepository
        .findAllByPid(pid)
        .collectList()
}
