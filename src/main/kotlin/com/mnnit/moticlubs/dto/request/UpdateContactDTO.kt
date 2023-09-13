package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateContactDTO(
    @JsonProperty("contact")
    val contact: String,
)
