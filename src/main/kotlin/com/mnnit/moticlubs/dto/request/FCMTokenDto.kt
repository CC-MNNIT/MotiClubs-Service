package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class FCMTokenDto(
    @JsonProperty("token")
    val token: String,
)
