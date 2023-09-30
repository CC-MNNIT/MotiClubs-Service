package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.utils.Validator

data class MembersDTO(
    @JsonProperty("cid")
    val cid: Long,

    @JsonProperty("chid")
    val chid: Long,

    @JsonProperty("users")
    val users: List<Long>,
) : Validator() {

    override fun validate(): Boolean = users.isNotEmpty()
}
