package com.mnnit.moticlubs.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.mnnit.moticlubs.dao.User

data class AdminUserDTO(
    @JsonProperty("uid")
    val uid: Long,

    @JsonProperty("cid")
    val clubId: Long,

    @JsonProperty("regNo")
    val regno: String,

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
) {
    constructor(clubId: Long, user: User) : this(
        uid = user.uid,
        clubId = clubId,
        regno = user.regno,
        name = user.name,
        email = user.email,
        course = user.course,
        phone = user.phone,
        avatar = user.avatar,
    )
}