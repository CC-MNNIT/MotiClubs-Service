package com.mnnit.moticlubs.utils

abstract class Validator {

    companion object {
        val EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@mnnit.ac.in$"
            .toRegex()
        val URL_REGEX =
            "^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b[-a-zA-Z0-9()@:%_+.~#?&/=]*$"
                .toRegex()
        val PHONE_REGEX = "^[0-9]{10}$".toRegex()
        val REG_NO_REGEX = "^([0-9]{4})([0-9]([0-9]{3})|[a-zA-Z]{2,4}([0-9]{2}))$".toRegex()
    }

    abstract fun validate(): Boolean

    fun String.validateUrl(): Boolean = trimmedEmpty() || (length <= 512 && this matches URL_REGEX)

    fun String.validateColor(): Boolean = trimmedNotEmpty() && length == 6

    fun String.validateContact(): Boolean = trimmedEmpty() || (
        length <= 256 && (this matches EMAIL_REGEX || this matches PHONE_REGEX || this matches URL_REGEX)
        )

    fun String.validateName(): Boolean = trimmedNotEmpty() && length <= 256

    fun String.validateRegNo(): Boolean = trimmedNotEmpty() && length <= 256 && this matches REG_NO_REGEX

    fun String.validatePostMessage(): Boolean = trimmedNotEmpty() && length <= 2048

    fun String.validateReplyMessage(): Boolean = trimmedNotEmpty() && length <= 512

    fun String.validateClubDescription(): Boolean = trimmedNotEmpty() || length <= 1024

    fun String.validateClubSummary(): Boolean = trimmedNotEmpty() && length <= 512

    private fun String.trimmedEmpty(): Boolean = trim().isEmpty()
    private fun String.trimmedNotEmpty(): Boolean = trim().isNotEmpty()
}
