package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.utils.Validator

data class FCMTokenDTO(
    @JsonProperty("token")
    val token: String,
) : Validator() {

    override fun validate(): Boolean = token.validateName()
}
