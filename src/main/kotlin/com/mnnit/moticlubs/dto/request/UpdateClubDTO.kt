package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateClubDTO(
    @JsonProperty("description")
    val description: String,

    @JsonProperty("avatar")
    val avatar: String,

    @JsonProperty("summary")
    val summary: String,
)
