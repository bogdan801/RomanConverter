package com.bogdan801.romanconverter.data.repository

import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import com.bogdan801.romanconverter.domain.model.QuizType
import com.bogdan801.romanconverter.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class RepositoryImpl(
    //private val dao: Dao
) : Repository {
    private val roman = MutableStateFlow(listOf<LeaderboardItem>())
    private val arabic = MutableStateFlow(listOf<LeaderboardItem>())
    private val both = MutableStateFlow(listOf<LeaderboardItem>())

    override suspend fun saveRecord(item: LeaderboardItem, type: QuizType) {
        when(type){
            QuizType.GuessRoman -> {
                roman.update {
                    it.toMutableList().apply { add(item) }.sortedByDescending { item -> item.score }
                }
            }
            QuizType.GuessArabic -> {
                arabic.update {
                    it.toMutableList().apply { add(item) }.sortedByDescending { item -> item.score }
                }
            }
            QuizType.GuessBoth -> {
                both.update {
                    it.toMutableList().apply { add(item) }.sortedByDescending { item -> item.score }
                }
            }
        }

    }

    override fun getRomanLeaderboardFlow(): Flow<List<LeaderboardItem>> {
        return roman
    }

    override fun getArabicLeaderboardFlow(): Flow<List<LeaderboardItem>> {
        return arabic
    }

    override fun getBothLeaderboardFlow(): Flow<List<LeaderboardItem>> {
        return both
    }

    override suspend fun deleteRecord(id: Int, quizType: QuizType) {
        when(quizType){
            QuizType.GuessRoman -> {
                roman.value.indexOfFirst { it.id == id }.let { i ->
                    if(i != -1) {
                        roman.update {
                            it.toMutableList().apply { removeAt(i) }
                        }
                    }
                }
            }
            QuizType.GuessArabic -> {
                arabic.value.indexOfFirst { it.id == id }.let { i ->
                    if(i != -1) {
                        arabic.update {
                            it.toMutableList().apply { removeAt(i) }
                        }
                    }
                }
            }
            QuizType.GuessBoth -> {
                both.value.indexOfFirst { it.id == id }.let { i ->
                    if(i != -1) {
                        both.update {
                            it.toMutableList().apply { removeAt(i) }
                        }
                    }
                }
            }
        }
    }

    override suspend fun clearLeaderboardOfAType(quizType: QuizType) {
        when(quizType){
            QuizType.GuessRoman -> {
                roman.update { listOf() }
            }
            QuizType.GuessArabic -> {
                arabic.update { listOf() }
            }
            QuizType.GuessBoth -> {
                both.update { listOf() }
            }
        }
    }
}