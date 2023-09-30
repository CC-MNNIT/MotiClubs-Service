package com.mnnit.moticlubs.dao

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.utils.Validator
import org.springframework.data.annotation.Id

data class Channel(
    @JsonProperty("cid")
    val cid: Long,

    @Id
    @JsonProperty("chid")
    val chid: Long = System.currentTimeMillis(),

    @JsonProperty("name")
    val name: String,

    @JsonProperty("private")
    val private: Boolean,
) : Validator() {

    constructor(map: Map<String, Any>) : this(
        cid = map[Channel::cid.name].toString().toLong(),
        chid = map[Channel::chid.name].toString().toLong(),
        name = map[Channel::name.name].toString(),
        private = map[Channel::private.name].toString().toInt() == 1,
    )

    fun toHashMap(): HashMap<String, String> = HashMap<String, String>().apply {
        this["ch_${Channel::cid.name}"] = cid.toString()
        this["ch_${Channel::chid.name}"] = chid.toString()
        this["ch_${Channel::name.name}"] = name
        this["ch_${Channel::private.name}"] = if (private) "1" else "0"
    }

    override fun validate(): Boolean = name.validateName()
}
