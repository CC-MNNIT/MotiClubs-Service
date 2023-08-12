package com.mnnit.moticlubs.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class Club(
    @Id
    @JsonProperty("cid")
    val cid: Long = System.currentTimeMillis(),

    @JsonProperty("name")
    val name: String,

    @JsonProperty("description")
    val description: String,

    @JsonProperty("avatar")
    val avatar: String = "",

    @JsonProperty("summary")
    val summary: String,
)
