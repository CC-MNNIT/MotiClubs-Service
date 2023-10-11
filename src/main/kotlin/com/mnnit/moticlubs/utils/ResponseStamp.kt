package com.mnnit.moticlubs.utils

object ResponseStamp {

    private val LOGGER = ServiceLogger.getLogger(ResponseStamp::class.java)

    abstract class StampKey(private var keyValue: String = "") {

        fun withKey(key: String): StampKey {
            keyValue = "$keyValue:$key"
            return this
        }

        fun getKey(): String = keyValue
    }

    val NONE get() = object : StampKey("NONE") {}
    val ADMIN get() = object : StampKey("ADMIN") {}
    val CHANNEL get() = object : StampKey("CHANNEL") {}
    val CLUB get() = object : StampKey("CLUB") {}
    val MEMBER get() = object : StampKey("MEMBER") {}
    val POST get() = object : StampKey("POST") {}
    val REPLY get() = object : StampKey("REPLY") {}
    val URL get() = object : StampKey("URL") {}
    val USER get() = object : StampKey("USER") {}

    private val LAST_UPDATED_MAP = HashMap<String, Long>()

    @Synchronized
    fun StampKey.get(updatedLast: Long): Long = LAST_UPDATED_MAP.getOrDefault(this.getKey(), updatedLast)

    @Synchronized
    fun StampKey.getString(updatedLast: Long): String = get(updatedLast).toString()

    @Synchronized
    fun StampKey.validateLast(updatedLast: Long): Boolean {
        val lastUpdatedForKey = LAST_UPDATED_MAP.getOrDefault(this.getKey(), -1)
        if (lastUpdatedForKey == -1L) {
            val stamp = System.currentTimeMillis()
            LOGGER.info("[CACHE] ${this.getKey()} key first fetch. returning - $stamp")
            LAST_UPDATED_MAP[this.getKey()] = stamp
            return false
        }

        if (lastUpdatedForKey == updatedLast) {
            LOGGER.info("[CACHE] ${this.getKey()} key not invalidated. returning - $lastUpdatedForKey")
            return true
        }

        return false
    }

    @Synchronized
    fun StampKey.invalidateStamp(): Long {
        val keys = LAST_UPDATED_MAP.keys.filter { it.contains(this.getKey()) }
        val updatedStamp = System.currentTimeMillis()

        LOGGER.info("[CACHE] invalidating: $updatedStamp to filtered keys: $keys")
        keys.forEach { LAST_UPDATED_MAP[it] = updatedStamp }
        return updatedStamp
    }
}
