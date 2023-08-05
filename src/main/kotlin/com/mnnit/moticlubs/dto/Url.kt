package com.mnnit.moticlubs.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class Url(
    @Id
    @JsonProperty("urlId")
    val urlId: Long,

    @JsonProperty("cid")
    val cid: Long,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("color")
    val color: String,

    @JsonProperty("url")
    val url: String,
)
