package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.utils.Validator

data class UpdatePostDTO(
    @JsonProperty("message")
    val message: String,

    @JsonProperty("updated")
    val updated: Long,
) : Validator() {

    override fun validate(): Boolean = message.validatePostMessage()
}
