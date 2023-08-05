package com.mnnit.moticlubs.web.config

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class GlobalErrorAttributes : DefaultErrorAttributes() {

    override fun getErrorAttributes(
        request: ServerRequest?,
        options: ErrorAttributeOptions?,
    ): MutableMap<String, Any> = super.getErrorAttributes(
        request,
        ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE),
    )
}
