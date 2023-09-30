package com.mnnit.moticlubs.dao

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.utils.Validator
import org.springframework.data.annotation.Id

data class Post(
    @Id
    @JsonProperty("pid")
    val pid: Long = System.currentTimeMillis(),

    @JsonProperty("uid")
    val uid: Long,

    @JsonProperty("chid")
    val chid: Long,

    @JsonProperty("updated")
    val updated: Long = System.currentTimeMillis(),

    @JsonProperty("message")
    val message: String,
) : Validator() {

    fun toHashMap(): HashMap<String, String> = HashMap<String, String>().apply {
        this["p_${Post::pid.name}"] = pid.toString()
        this["p_${Post::uid.name}"] = uid.toString()
        this["p_${Post::chid.name}"] = chid.toString()
        this["p_${Post::updated.name}"] = updated.toString()
        this["p_${Post::message.name}"] = message
    }

    override fun validate(): Boolean = message.validatePostMessage()
}
