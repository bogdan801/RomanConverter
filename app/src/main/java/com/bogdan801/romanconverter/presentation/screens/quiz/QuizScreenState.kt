package com.bogdan801.romanconverter.presentation.screens.quiz

import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import com.bogdan801.romanconverter.domain.model.QuizType
import kotlin.random.Random

data class QuizScreenState(
    val selectedType: QuizType = QuizType.GuessRoman,
    val romanLeaderboard: List<LeaderboardItem> = listOf(),
    val arabicLeaderboard: List<LeaderboardItem> = listOf(),
    val bothLeaderboard: List<LeaderboardItem> = listOf(),
    val lastDeletedItems: List<LeaderboardItem> = listOf(),
    val isQuizStarted: Boolean = false,
    val currentQuizType: QuizType = QuizType.GuessRoman,
    val currentValueToGuess: String = "",
    val currentInputValue: String = "",
    val currentCount: Int = 0,
    val currentTime: Int = 60,
    val currentScore: Int = 0,
    val levelStep: Int = 3
)
