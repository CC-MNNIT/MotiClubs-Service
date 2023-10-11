package com.mnnit.moticlubs.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ImageUrlDTO(
    @JsonProperty("url")
    val url: String,
)
