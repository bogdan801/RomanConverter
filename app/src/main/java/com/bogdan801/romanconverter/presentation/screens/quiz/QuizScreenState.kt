package com.bogdan801.romanconverter.presentation.screens.quiz

import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import com.bogdan801.romanconverter.domain.model.QuizType

data class QuizScreenState(
    val selectedType: QuizType = QuizType.GuessRoman,
    val isQuizStarted: Boolean = false,
    val romanLeaderboard: List<LeaderboardItem> = listOf(),
    val arabicLeaderboard: List<LeaderboardItem> = listOf(),
    val bothLeaderboard: List<LeaderboardItem> = listOf(),
    val lastDeletedItems: List<LeaderboardItem> = listOf()
)
