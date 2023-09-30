package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.utils.Validator

data class UpdateContactDTO(
    @JsonProperty("contact")
    val contact: String,
) : Validator() {

    override fun validate(): Boolean = contact.validateContact()
}
