package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.utils.Validator

data class UpdateClubDTO(
    @JsonProperty("description")
    val description: String,

    @JsonProperty("avatar")
    val avatar: String,

    @JsonProperty("summary")
    val summary: String,
) : Validator() {

    override fun validate(): Boolean = description.validateClubDescription() &&
        summary.validateClubSummary() &&
        avatar.validateUrl()
}
