package com.mnnit.moticlubs

import com.mnnit.moticlubs.utils.ResponseStamp
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono

class SampleTests {

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

    @Test
    fun validateStampFilter() {
        val map = HashMap<String, Boolean>().apply {
            this[ResponseStamp.USER.getKey()] = false
            this[ResponseStamp.ADMIN.getKey()] = false
            this[ResponseStamp.MEMBER.withKey("23").withKey("69").getKey()] = false
            this[ResponseStamp.MEMBER.withKey("23").withKey("6").getKey()] = false
            this[ResponseStamp.MEMBER.withKey("23").getKey()] = false
            this[ResponseStamp.MEMBER.withKey("69").getKey()] = false
            this[ResponseStamp.MEMBER.getKey()] = false
        }

        val keys = map.keys.filter { it.contains(ResponseStamp.MEMBER.withKey("23").getKey()) }
        keys.forEach { map[it] = true }

        assert(map["MEMBER:23:69"] ?: false)
        assert(map["MEMBER:23:6"] ?: false)
        assert(map["MEMBER:23"] ?: false)
    }

    @Test
    fun testIndependentTask() {
        var num = 0
        Mono.just(num)
            .flatMap {
                Thread {
                    Thread.sleep(1500)
                    println("Thread done with $num")
                }.start()

                num++
                println("Num: $it")
                Mono.just(it)
            }
            .subscribe()
        println("Finished with $num:")

        num++
        Thread.sleep(3000)
    }
}
