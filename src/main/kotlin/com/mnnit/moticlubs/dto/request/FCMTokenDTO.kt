package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class FCMTokenDTO(
    @JsonProperty("token")
    val token: String,
)
