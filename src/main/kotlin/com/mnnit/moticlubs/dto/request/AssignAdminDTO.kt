package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.utils.Validator

data class AssignAdminDTO(
    @JsonProperty("cid")
    val cid: Long,

    @JsonProperty("regNo")
    val regNo: String,
) : Validator() {

    override fun validate(): Boolean = regNo.validateRegNo()
}
