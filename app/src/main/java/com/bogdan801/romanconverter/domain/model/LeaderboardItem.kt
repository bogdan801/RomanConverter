package com.bogdan801.romanconverter.domain.model

import java.time.LocalDate
import kotlin.random.Random

data class LeaderboardItem(
    val id: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val count: Int = Random.nextInt(5, 25),
    val score: Int = Random.nextInt(10000, 25000)
)
