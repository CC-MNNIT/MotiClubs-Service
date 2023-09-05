package com.mnnit.moticlubs.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class Club(
    @Id
    @JsonProperty("cid")
    val cid: Long = System.currentTimeMillis(),

    @JsonProperty("name")
    val name: String,

    @JsonProperty("description")
    val description: String,

    @JsonProperty("avatar")
    val avatar: String = "",

    @JsonProperty("summary")
    val summary: String,
) {

    fun toHashMap(): HashMap<String, String> = HashMap<String, String>().apply {
        this["c_${Club::cid.name}"] = cid.toString()
        this["c_${Club::name.name}"] = name
        this["c_${Club::description.name}"] = description
        this["c_${Club::summary.name}"] = summary
        this["c_${Club::avatar.name}"] = avatar
    }
}
