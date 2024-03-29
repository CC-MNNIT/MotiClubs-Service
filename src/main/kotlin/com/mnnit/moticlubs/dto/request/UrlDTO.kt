package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.utils.Validator

data class UrlDTO(
    @JsonProperty("urlId")
    val urlid: Long,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("color")
    val color: String,

    @JsonProperty("url")
    val url: String,
) : Validator() {

    override fun validate(): Boolean = name.validateName() && color.validateColor() && url.validateUrl()
}
