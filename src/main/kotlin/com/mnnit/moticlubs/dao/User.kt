package com.mnnit.moticlubs.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class User(
    @Id
    @JsonProperty("uid")
    val uid: Long = System.currentTimeMillis(),

    @JsonProperty("regNo")
    val regno: String,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("email")
    val email: String,

    @JsonProperty("course")
    val course: String,

    @JsonProperty("branch")
    val branch: String,

    @JsonProperty("avatar")
    val avatar: String,
)
