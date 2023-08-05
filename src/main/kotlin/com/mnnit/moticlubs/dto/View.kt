package com.mnnit.moticlubs.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class View(
    @JsonProperty("pid")
    val pid: Long,

    @Id
    @JsonProperty("uid")
    val uid: Long,
)
