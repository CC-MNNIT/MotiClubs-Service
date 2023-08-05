package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class SavePostDTO(
    @JsonProperty("pid")
    val pid: Long,

    @JsonProperty("uid")
    val uid: Long,

    @JsonProperty("chid")
    val chid: Long,

    @JsonProperty("cid")
    val cid: Long,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("general")
    val general: Int,
)
