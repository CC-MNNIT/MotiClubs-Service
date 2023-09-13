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

    @JsonProperty("contact")
    val contact: String,
) {
    constructor(map: Map<String, Any>) : this(
        uid = map[User::uid.name].toString().toLong(),
        regno = map[User::regno.name].toString(),
        name = map[User::name.name].toString(),
        email = map[User::email.name].toString(),
        course = map[User::course.name].toString(),
        branch = map[User::branch.name].toString(),
        avatar = map[User::avatar.name].toString(),
        contact = map[User::contact.name].toString(),
    )

    fun toHashMap(): HashMap<String, String> = HashMap<String, String>().apply {
        this["u_${User::uid.name}"] = uid.toString()
        this["u_${User::regno.name}"] = regno
        this["u_${User::name.name}"] = name
        this["u_${User::email.name}"] = email
        this["u_${User::course.name}"] = course
        this["u_${User::branch.name}"] = branch
        this["u_${User::avatar.name}"] = avatar
        this["u_${User::contact.name}"] = contact
    }
}
