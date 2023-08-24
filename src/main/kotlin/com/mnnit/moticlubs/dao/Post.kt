package com.mnnit.moticlubs.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class Post(
    @Id
    @JsonProperty("pid")
    val pid: Long = -1,

    @JsonProperty("uid")
    val uid: Long,

    @JsonProperty("chid")
    val chid: Long,

    @JsonProperty("message")
    val message: String,
)
