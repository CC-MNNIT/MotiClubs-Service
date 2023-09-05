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
) {

    fun toHashMap(): HashMap<String, String> = HashMap<String, String>().apply {
        this["p_${Post::pid.name}"] = pid.toString()
        this["p_${Post::uid.name}"] = uid.toString()
        this["p_${Post::chid.name}"] = chid.toString()
        this["p_${Post::message.name}"] = message
    }
}
