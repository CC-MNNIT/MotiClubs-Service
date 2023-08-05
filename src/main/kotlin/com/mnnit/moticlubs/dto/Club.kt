package com.mnnit.moticlubs.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.dto.request.UpdateClubDTO
import org.springframework.data.annotation.Id

data class Club(
    @Id
    @JsonProperty("cid")
    val cid: Long = System.currentTimeMillis(),

    @JsonProperty("name")
    val name: String,

    @JsonProperty("description")
    val description: String,

    @JsonProperty("avatar")
    val avatar: String,

    @JsonProperty("summary")
    val summary: String,
) {
    fun copy(updateClubDTO: UpdateClubDTO) = this.copy(
        description = updateClubDTO.description,
        avatar = updateClubDTO.avatar,
        summary = updateClubDTO.summary
    )
}
