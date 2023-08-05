package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateChannelDTO(
    @JsonProperty("cid")
    val cid: Long,

    @JsonProperty("name")
    val name: String,
)
