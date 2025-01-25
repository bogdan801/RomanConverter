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
    val romanLeaderboardLoading: Boolean = false,
    val arabicLeaderboardLoading: Boolean = false,
    val bothLeaderboardLoading: Boolean = false,
    val romanLeaderboard: LeaderboardData = LeaderboardData(),
    val arabicLeaderboard: LeaderboardData = LeaderboardData(),
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
