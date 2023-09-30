package com.mnnit.moticlubs.dao

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.utils.Validator
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
) : Validator() {

    fun toHashMap(): HashMap<String, String> = HashMap<String, String>().apply {
        this["r_${Reply::time.name}"] = time.toString()
        this["r_${Reply::pid.name}"] = pid.toString()
        this["r_${Reply::uid.name}"] = uid.toString()
        this["r_${Reply::message.name}"] = message
    }

    override fun validate(): Boolean = message.validateReplyMessage()
}
