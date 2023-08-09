package com.mnnit.moticlubs.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class Url(
    @Id
    @JsonProperty("urlId")
    val urlid: Long,

    @JsonProperty("cid")
    val cid: Long,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("color")
    val color: String,

    @JsonProperty("url")
    val url: String,
)
