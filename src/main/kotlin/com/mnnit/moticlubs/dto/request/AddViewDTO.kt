package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class AddViewDTO(
    @JsonProperty("pid")
    val pid: Long,
)
