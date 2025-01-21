package com.bogdan801.romanconverter.domain.repository

import com.bogdan801.romanconverter.domain.model.RecordItem
import com.bogdan801.romanconverter.domain.model.QuizType
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun saveRecord(item: RecordItem, type: QuizType)
    suspend fun saveRecords(items: List<RecordItem>, type: QuizType)

    suspend fun updateLastRecord(item: RecordItem)
    fun getRomanRecordsFlow(): Flow<List<RecordItem>>
    fun getArabicRecordsFlow(): Flow<List<RecordItem>>
    fun getBothRecordsFlow(): Flow<List<RecordItem>>

    suspend fun deleteRecord(id: Int, quizType: QuizType)

    suspend fun clearRecordsOfAType(quizType: QuizType)
}