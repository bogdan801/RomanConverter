package com.bogdan801.romanconverter.presentation.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.romanconverter.data.util.convertArabicToRoman
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
import kotlin.random.Random

@HiltViewModel
class QuizViewModel
@Inject
constructor(
    private val repository: Repository
): ViewModel() {
    private val _screenState = MutableStateFlow(QuizScreenState())
    val screenState = _screenState.asStateFlow()

    //setters
    fun setType(newValue: QuizType){
        _screenState.update {
            it.copy(
                selectedType = newValue
            )
        }
    }

    private fun setCurrentQuizType(quizType: QuizType){
        _screenState.update {
            it.copy(
                currentQuizType = quizType
            )
        }
    }

    private fun setValueToGuess(value: String){
        _screenState.update {
            it.copy(
                currentValueToGuess = value
            )
        }
    }

    fun setInputValue(value: String){
        _screenState.update {
            it.copy(
                currentInputValue = value
            )
        }
    }

    private fun setCount(value: Int){
        _screenState.update {
            it.copy(
                currentCount = value
            )
        }
    }

    private fun incrementCurrentCount(){
        _screenState.update {
            it.copy(
                currentCount = _screenState.value.currentCount + 1
            )
        }
    }

    fun setTime(value: Int){
        _screenState.update {
            it.copy(
                currentTime = value
            )
        }
    }

    private fun incrementTime(valueToAdd: Int){
        _screenState.update {
            it.copy(
                currentTime = _screenState.value.currentTime + valueToAdd
            )
        }
    }

    private fun setScore(value: Int){
        _screenState.update {
            it.copy(
                currentScore = value
            )
        }
    }

    private fun incrementScore(valueToAdd: Int){
        _screenState.update {
            it.copy(
                currentScore = _screenState.value.currentScore + valueToAdd
            )
        }
    }

    //leaderboard logic
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

    //quiz logic
    fun startQuiz(newValue: Boolean, homeViewModel: HomeViewModel) {
        _screenState.update {
            it.copy(
                isQuizStarted = newValue
            )
        }
        homeViewModel.showNavBar(!newValue)

        if(newValue) setupTheQuiz()
    }

    private fun nextValueToGuess(): String {
        val type = _screenState.value.currentQuizType
        //val count = _screenState.value.currentCount
        return when(type){
            QuizType.GuessRoman -> {
                convertArabicToRoman(Random.nextInt(0, 3999).toString())
            }
            QuizType.GuessArabic -> {
                Random.nextInt(0, 3999).toString()
            }
            QuizType.GuessBoth -> throw Exception("Current type can not be Both")
        }
    }

    private fun setupTheQuiz(){
        _screenState.update {
            it.copy(
                currentQuizType = when(_screenState.value.selectedType){
                    QuizType.GuessRoman -> QuizType.GuessRoman
                    QuizType.GuessArabic -> QuizType.GuessArabic
                    QuizType.GuessBoth -> {
                        if (Random.nextInt(2) == 1) QuizType.GuessRoman
                        else QuizType.GuessArabic
                    }
                },
                currentCount = 0,
                currentTime = 60,
                currentInputValue = ""
            )
        }
        setValueToGuess(nextValueToGuess())
    }

    fun successfulGuess() {
        incrementCurrentCount()
        incrementTime(10)
        incrementScore(100)
        if(_screenState.value.selectedType == QuizType.GuessBoth) {
            setCurrentQuizType(
                if (Random.nextInt(2) == 1) QuizType.GuessRoman
                else QuizType.GuessArabic
            )
        }
        setValueToGuess(nextValueToGuess())
        setInputValue("")
    }

    fun gameOver() {

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