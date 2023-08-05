package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateAvatarDTO(
    @JsonProperty("avatar")
    val avatar: String,
)
