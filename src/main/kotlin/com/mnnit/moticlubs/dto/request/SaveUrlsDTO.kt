package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class SaveUrlsDTO(
    @JsonProperty("urls")
    val urls: List<UrlDTO>,
)
