package com.mnnit.moticlubs.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class Admin(
    @JsonProperty("cid")
    val cid: Long,

    @Id
    @JsonProperty("uid")
    val uid: Long,
)
