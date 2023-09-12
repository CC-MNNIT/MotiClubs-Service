package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdatePostDTO(
    @JsonProperty("message")
    val message: String,

    @JsonProperty("updated")
    val updated: Long,
)
