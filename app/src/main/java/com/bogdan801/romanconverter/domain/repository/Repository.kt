package com.bogdan801.romanconverter.domain.repository

import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import com.bogdan801.romanconverter.domain.model.QuizType
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun saveRecord(item: LeaderboardItem, type: QuizType)
    fun getRomanLeaderboardFlow(): Flow<List<LeaderboardItem>>
    fun getArabicLeaderboardFlow(): Flow<List<LeaderboardItem>>
    fun getBothLeaderboardFlow(): Flow<List<LeaderboardItem>>

    suspend fun deleteRecord(id: Int, quizType: QuizType)

    suspend fun clearLeaderboardOfAType(quizType: QuizType)
}