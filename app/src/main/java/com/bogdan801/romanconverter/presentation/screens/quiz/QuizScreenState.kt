package com.bogdan801.romanconverter.presentation.screens.quiz

import com.bogdan801.romanconverter.domain.model.LeaderboardData
import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import com.bogdan801.romanconverter.domain.model.RecordItem
import com.bogdan801.romanconverter.domain.model.QuizType

data class QuizScreenState(
    val selectedType: QuizType = QuizType.GuessRoman,

    //leaderboard
    val isUserLoggedIn: Boolean = false,
    val userID: String = "",
    val isLeaderboardLoading: Boolean = false,
    val romanLeaderboard: LeaderboardData = LeaderboardData(),
    val arabicLeaderboard: LeaderboardData = LeaderboardData(
        records = listOf(
            LeaderboardItem(
                rank = 1,
                username = "PrivateWolf 352",
                score = 32342
            ),
            LeaderboardItem(
                rank = 2,
                username = "Palanquin3",
                score = 31463
            ),
            LeaderboardItem(
                rank = 3,
                username = "Slaughterhaus23",
                score = 26777
            ),
            LeaderboardItem(
                rank = 4,
                username = "Maradonner42",
                score = 19032
            ),
            LeaderboardItem(
                rank = 5,
                username = "Palanquin3",
                score = 31463
            ),
            LeaderboardItem(
                rank = 6,
                username = "Slaughterhaus23",
                score = 26777
            ),
            LeaderboardItem(
                rank = 7,
                username = "Maradonner42",
                score = 19032,
                isUser = true
            )
        )
    ),
    val bothLeaderboard: LeaderboardData = LeaderboardData(),

    //records
    val romanRecords: List<RecordItem> = listOf(),
    val arabicRecords: List<RecordItem> = listOf(),
    val bothRecords: List<RecordItem> = listOf(),
    val lastDeletedItems: List<RecordItem> = listOf(),

    //quiz
    val isQuizStarted: Boolean = false,
    val currentQuizType: QuizType = QuizType.GuessRoman,
    val currentValueToGuess: String = "",
    val currentInputValue: String = "",
    val currentCount: Int = 0,
    val currentTime: Int = 60,
    val currentScore: Int = 0,
    val levelStep: Int = 3
)
