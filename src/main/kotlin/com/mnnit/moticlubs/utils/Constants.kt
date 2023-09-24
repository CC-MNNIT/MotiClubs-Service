package com.mnnit.moticlubs.utils

object Constants {
    val EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@mnnit.ac.in$".toRegex()

    const val STAMP_HEADER = "X-Stamp-Value"

    const val BASE_PATH = "api/v1"

    const val USER_ROUTE = "user"
    const val CLUBS_ROUTE = "clubs"
    const val POSTS_ROUTE = "posts"
    const val SUPER_ADMIN_ROUTE = "admin"
    const val CHANNEL_ROUTE = "channel"
    const val URL_ROUTE = "url"
    const val VIEWS_ROUTE = "views"
    const val REPLY_ROUTE = "reply"

    const val USER_ID_CLAIM = "userId"
    const val CLUB_ID_CLAIM = "clubId"
    const val CHANNEL_ID_CLAIM = "channelId"
    const val POST_ID_CLAIM = "postId"
}
