package com.bogdan801.romanconverter.presentation.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.romanconverter.data.util.convertArabicToRoman
import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import com.bogdan801.romanconverter.domain.model.QuizType
import com.bogdan801.romanconverter.domain.repository.Repository
import com.bogdan801.romanconverter.presentation.screens.home.HomeViewModel
import com.bogdan801.util_library.mapRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

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

    fun setGameTimer(value: Int){
        _screenState.update {
            it.copy(
                currentTime = value
            )
        }
    }

    private fun incrementGameTimer(valueToAdd: Int){
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

    private fun saveRecord(leaderboardItem: LeaderboardItem){
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
    fun startQuiz(homeViewModel: HomeViewModel) {
        _screenState.update {
            it.copy(
                isQuizStarted = true
            )
        }
        homeViewModel.showNavBar(false)

        setupTheQuiz()
    }

    fun stopQuiz(homeViewModel: HomeViewModel) {
        _screenState.update {
            it.copy(
                isQuizStarted = false
            )
        }
        homeViewModel.showNavBar(true)
    }


    private fun nextValueToGuess(): String {
        if(_screenState.value.selectedType == QuizType.GuessBoth) {
            setCurrentQuizType(
                if (Random.nextInt(2) == 1) QuizType.GuessRoman
                else QuizType.GuessArabic
            )
        }
        val type = _screenState.value.currentQuizType
        val count = _screenState.value.currentCount

        val numberRange = when(count){
            in 0..4   -> 1..39
            in 5..9   -> 40..89
            in 10..14 -> 90..399
            in 15..19 -> 400..899
            in 20..24 -> 900..3999
            in 25..29 -> 4000..8999
            in 30..34 -> 9000..39999
            in 35..39 -> 40000..89999
            in 40..44 -> 90000..399999
            in 45..49 -> 400000..899999
            else            -> 900000..3999999
        }

        return when(type){
            QuizType.GuessRoman -> {
                convertArabicToRoman(Random.nextInt(numberRange).toString())
            }
            QuizType.GuessArabic -> {
                Random.nextInt(numberRange).toString()
            }
            QuizType.GuessBoth -> throw Exception("Current type can not be Both")
        }
    }

    private var time: Int = _screenState.value.currentTime
    fun setupTheQuiz(){
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
                currentScore = 0,
                currentTime = 60,
                currentInputValue = ""
            )
        }
        setValueToGuess(nextValueToGuess())
        time = _screenState.value.currentTime
    }

    fun successfulGuess() {
        val newTime = _screenState.value.currentTime
        val deltaTime = time - newTime

        val timeToAdd: Int
        val minScore: Int
        val maxScore: Int
        when(_screenState.value.currentCount){
            in 0..4 -> {
                timeToAdd = 10
                minScore = 25
                maxScore = 50
            }
            in 5..9 -> {
                timeToAdd = 10
                minScore = 50
                maxScore = 100
            }
            in 10..14 -> {
                timeToAdd = 10
                minScore = 75
                maxScore = 150
            }
            in 15..19 -> {
                timeToAdd = 10
                minScore = 100
                maxScore = 200
            }
            in 20..24 -> {
                timeToAdd = 15
                minScore = 125
                maxScore = 250
            }
            in 25..29 -> {
                timeToAdd = 15
                minScore = 150
                maxScore = 300
            }
            in 30..34 -> {
                timeToAdd = 15
                minScore = 175
                maxScore = 350
            }
            in 35..39 -> {
                timeToAdd = 15
                minScore = 200
                maxScore = 400
            }
            in 40..44 -> {
                timeToAdd = 20
                minScore = 225
                maxScore = 450
            }
            in 45..49 -> {
                timeToAdd = 20
                minScore = 250
                maxScore = 500
            }
            else -> {
                timeToAdd = 20
                minScore = 275
                maxScore = 550
            }
        }

        incrementGameTimer(timeToAdd)
        incrementScore(mapRange(deltaTime, 0..timeToAdd, minScore..maxScore))
        incrementCurrentCount()
        setValueToGuess(nextValueToGuess())
        setInputValue("")

        time = _screenState.value.currentTime
    }

    fun quizOver() {
        saveRecord(
            LeaderboardItem(
                id = Random.nextInt(100000, 999999),
                count = _screenState.value.currentCount,
                score = _screenState.value.currentScore
            )
        )
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