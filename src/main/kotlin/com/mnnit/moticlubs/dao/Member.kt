package com.mnnit.moticlubs.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class Member(
    @JsonProperty("chid")
    val chid: Long,

    @Id
    @JsonProperty("uid")
    val uid: Long,
)
