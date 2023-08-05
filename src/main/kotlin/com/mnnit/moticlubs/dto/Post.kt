package com.mnnit.moticlubs.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.dto.request.SavePostDTO
import org.springframework.data.annotation.Id

data class Post(
    @Id
    @JsonProperty("pid")
    val pid: Long = System.currentTimeMillis(),

    @JsonProperty("uid")
    val uid: Long,

    @JsonProperty("chid")
    val chid: Long,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("general")
    val general: Int,
) {
    constructor(dto: SavePostDTO) : this(
        pid = dto.pid,
        uid = dto.uid,
        chid = dto.chid,
        message = dto.message,
        general = dto.general,
    )
}
