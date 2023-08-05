package com.mnnit.moticlubs.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class Subscriber(
    @JsonProperty("cid")
    val cid: Long,

    @Id
    @JsonProperty("uid")
    val uid: Long,
)
