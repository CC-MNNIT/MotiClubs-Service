package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.utils.Validator

data class SaveUrlsDTO(
    @JsonProperty("urls")
    val urls: List<UrlDTO>,
) : Validator() {

    override fun validate(): Boolean = urls.all { it.validate() }
}
