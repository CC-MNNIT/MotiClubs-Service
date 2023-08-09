package com.mnnit.moticlubs.dao

import org.springframework.data.annotation.Id

data class SuperAdmin(
    @Id
    val uid: Long,
)
