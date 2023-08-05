package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.dto.Url

data class SaveUrlsDTO(
    @JsonProperty("urls")
    val urls: List<Url>,
)
