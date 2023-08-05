package com.mnnit.moticlubs.dto

import org.springframework.data.annotation.Id

data class SuperAdmin(
    @Id
    val uid: Long,
)
