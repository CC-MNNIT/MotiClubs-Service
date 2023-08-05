package com.mnnit.moticlubs

import org.junit.jupiter.api.Test

class PathMatcherTest {

    @Test
    fun validateMatching() {
        val path = "api/v1"
        val matcherRegex = "^/$path(/user(/fromId|/fcm)|/admin(|/fromId))\$".toRegex()

        val testPathUser = "/$path/user/fromId"
        val testPathUserDiff = "/$path/user/fcm"
        val testPathUserWrong = "/$path/user/fail"

        val testPathAdmin = "/$path/admin"
        val testPathAdminDiff = "/$path/admin/fromId"
        val testPathAdminWrong = "/$path/admin/fail"
        val testPathRandom = "/$path/random"

        assert(testPathUser matches matcherRegex)
        assert(testPathUserDiff matches matcherRegex)
        assert(!testPathUserWrong.matches(matcherRegex))

        assert(testPathAdmin matches matcherRegex)
        assert(testPathAdminDiff matches matcherRegex)
        assert(!testPathAdminWrong.matches(matcherRegex))
        assert(!testPathRandom.matches(matcherRegex))
    }
}