package com.mnnit.moticlubs.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class FCM(
    @Id
    @JsonProperty("uid")
    val uid: Long,

    @JsonProperty("token")
    val token: String,
)
