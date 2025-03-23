package com.bogdan801.romanconverter.domain.model

data class LeaderboardItem(
    val rank: Int,
    val userID: Int? = null,
    val username: String,
    val score: Int,
    val isUser: Boolean = false,
    val isPrivate: Boolean = false
)
