package com.bogdan801.romanconverter.domain.model

data class LeaderboardItem(
    val rank: Int,
    val username: String,
    val score: Int,
    val isUser: Boolean = false
)
