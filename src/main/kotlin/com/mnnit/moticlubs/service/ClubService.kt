package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.Club
import com.mnnit.moticlubs.dto.request.UpdateClubDTO
import com.mnnit.moticlubs.repository.ClubRepository
import com.mnnit.moticlubs.utils.storeCache
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ClubService(
    private val clubRepository: ClubRepository,
) {

    @CacheEvict("all_clubs", allEntries = true)
    fun saveClub(club: Club): Mono<Club> = clubRepository.save(club)

    @Cacheable("all_clubs")
    fun getAllClubs(): Mono<List<Club>> = clubRepository
        .findAll()
        .collectList()
        .storeCache()

    fun getClubByCID(cid: Long): Mono<Club> = clubRepository.findById(cid)

    @CacheEvict("all_clubs", allEntries = true)
    fun updateClub(cid: Long, dto: UpdateClubDTO): Mono<Club> = clubRepository.updateClub(cid, dto)

    fun clubExists(cid: Long): Mono<Boolean> = clubRepository.existsById(cid)

    @CacheEvict(
        cacheNames = ["all_clubs", "urls", "admins", "all_channels", "members", "post", "replies"],
        allEntries = true
    )
    fun deleteClubByCid(cid: Long): Mono<Void> = clubRepository.deleteById(cid)
}
