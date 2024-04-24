package com.bogdan801.romanconverter.domain.model

import java.time.LocalDate

data class LeaderboardItem(
    val id: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val count: Int = 0,
    val score: Int = 0
)
