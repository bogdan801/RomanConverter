package com.bogdan801.romanconverter.domain.model

data class LeaderboardData(
    val records: List<LeaderboardItem>? = null,
    val error: String? = null
)
