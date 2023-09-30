package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.utils.Validator

data class UpdateAvatarDTO(
    @JsonProperty("avatar")
    val avatar: String,
) : Validator() {

    override fun validate(): Boolean = avatar.isEmpty() || avatar.validateUrl()
}
