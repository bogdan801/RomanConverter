package com.bogdan801.romanconverter.presentation.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import com.bogdan801.romanconverter.domain.model.QuizType
import com.bogdan801.romanconverter.domain.repository.Repository
import com.bogdan801.romanconverter.presentation.screens.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel
@Inject
constructor(
    private val repository: Repository
): ViewModel() {
    private val _screenState = MutableStateFlow(QuizScreenState())
    val screenState = _screenState.asStateFlow()

    fun setType(newValue: QuizType){
        _screenState.update {
            it.copy(
                selectedType = newValue
            )
        }
    }

    fun startQuiz(newValue: Boolean, homeViewModel: HomeViewModel) {
        _screenState.update {
            it.copy(
                isQuizStarted = newValue
            )
        }
        homeViewModel.showNavBar(!newValue)
    }

    private fun setupRomanLeaderboard(list: List<LeaderboardItem>){
        _screenState.update {
            it.copy(
                romanLeaderboard = list
            )
        }
    }

    private fun setupArabicLeaderboard(list: List<LeaderboardItem>){
        _screenState.update {
            it.copy(
                arabicLeaderboard = list
            )
        }
    }

    private fun setupBothLeaderboard(list: List<LeaderboardItem>){
        _screenState.update {
            it.copy(
                bothLeaderboard = list
            )
        }
    }

    fun saveRecord(leaderboardItem: LeaderboardItem){
        viewModelScope.launch {
            repository.saveRecord(leaderboardItem, _screenState.value.selectedType)
        }
    }
    fun restoreRecords(){
        viewModelScope.launch {
            _screenState.value.lastDeletedItems.forEach{
                repository.saveRecord(it, _screenState.value.selectedType)
            }
        }
    }

    private fun setLastDeletedRecords(items: List<LeaderboardItem>){
        _screenState.update {
            it.copy(
                lastDeletedItems = items
            )
        }
    }

    fun deleteRecord(leaderboardItem: LeaderboardItem){
        setLastDeletedRecords(listOf(leaderboardItem))
        viewModelScope.launch {
            repository.deleteRecord(leaderboardItem.id, _screenState.value.selectedType)
        }
    }

    fun clearLeaderboard(){
        setLastDeletedRecords(
            when(_screenState.value.selectedType){
                QuizType.GuessRoman -> _screenState.value.romanLeaderboard
                QuizType.GuessArabic -> _screenState.value.arabicLeaderboard
                QuizType.GuessBoth -> _screenState.value.bothLeaderboard
            }
        )
        viewModelScope.launch {
            repository.clearLeaderboardOfAType(_screenState.value.selectedType)
        }
    }

    init {
        viewModelScope.launch {
            repository.getRomanLeaderboardFlow().collect{
                setupRomanLeaderboard(it)
            }
        }
        viewModelScope.launch {
            repository.getArabicLeaderboardFlow().collect{
                setupArabicLeaderboard(it)
            }
        }
        viewModelScope.launch {
            repository.getBothLeaderboardFlow().collect{
                setupBothLeaderboard(it)
            }
        }
    }

}