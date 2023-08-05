package com.mnnit.moticlubs.service

import com.mnnit.moticlubs.dao.ClubRepository
import com.mnnit.moticlubs.dto.Club
import com.mnnit.moticlubs.dto.request.UpdateClubDTO
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ClubService(
    private val clubRepository: ClubRepository,
) {

    fun saveClub(club: Club): Mono<Club> = clubRepository.save(club)

    fun getAllClubs(): Mono<List<Club>> = clubRepository
        .findAll()
        .collectList()

    fun getClubByCID(cid: Long): Mono<Club> = clubRepository.findById(cid)

    fun updateClub(cid: Long, updateClubDTO: UpdateClubDTO): Mono<Club> = clubRepository
        .findById(cid)
        .flatMap { saveClub(it.copy(updateClubDTO)) }

    fun clubExists(cid: Long): Mono<Boolean> = clubRepository.existsById(cid)

    fun deleteClubByCID(cid: Long): Mono<Void> = clubRepository.deleteById(cid)
}
