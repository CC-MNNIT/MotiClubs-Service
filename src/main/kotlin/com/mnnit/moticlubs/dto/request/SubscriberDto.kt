package com.mnnit.moticlubs.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class SubscriberDto(
    @JsonProperty("cid")
    val clubId: Long,
)
