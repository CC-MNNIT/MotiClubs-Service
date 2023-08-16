package com.mnnit.moticlubs.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class Reply(
    @Id
    @JsonProperty("time")
    val time: Long,

    @JsonProperty("pid")
    val pid: Long,

    @JsonProperty("uid")
    val uid: Long,

    @JsonProperty("message")
    val message: String,
)