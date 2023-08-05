package com.mnnit.moticlubs.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class User(
    @Id
    @JsonProperty("uid")
    val uid: Long = System.currentTimeMillis(),

    @JsonProperty("regno")
    val regNo: String,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("email")
    val email: String,

    @JsonProperty("course")
    val course: String,

    @JsonProperty("phone")
    val phone: String,

    @JsonProperty("avatar")
    val avatar: String,
)
