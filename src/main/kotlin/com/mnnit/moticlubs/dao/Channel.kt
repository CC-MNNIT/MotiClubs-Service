package com.mnnit.moticlubs.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class Channel(
    @JsonProperty("cid")
    val cid: Long,

    @Id
    @JsonProperty("chid")
    val chid: Long,

    @JsonProperty("name")
    val name: String,
)
