package com.mnnit.moticlubs.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ServiceLogger private constructor(clazz: Class<*>) {

    private val logger: Logger = LoggerFactory.getLogger(clazz)

    companion object {
        fun getLogger(clazz: Class<*>): ServiceLogger = ServiceLogger(clazz)
    }

    fun info(str: String) = logger.info("[${getReqId()}] $str")

    fun debug(str: String) = logger.debug("[${getReqId()}] $str")

    fun warn(str: String) = logger.warn("[${getReqId()}] $str")
}
