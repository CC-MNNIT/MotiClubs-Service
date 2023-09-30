package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.utils.Validator

data class UpdateChannelDTO(
    @JsonProperty("cid")
    val cid: Long,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("private")
    val private: Boolean,
) : Validator() {

    override fun validate(): Boolean = name.validateName()
}
