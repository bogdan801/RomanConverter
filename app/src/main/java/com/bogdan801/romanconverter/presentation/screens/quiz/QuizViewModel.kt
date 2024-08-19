package com.bogdan801.romanconverter.presentation.screens.quiz

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.romanconverter.data.util.convertArabicToRoman
import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import com.bogdan801.romanconverter.domain.model.QuizType
import com.bogdan801.romanconverter.domain.repository.Repository
import com.bogdan801.romanconverter.presentation.screens.home.HomeViewModel
import com.bogdan801.romanconverter.presentation.util.mapRange
import com.bogdan801.util_library.intSettings
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    fun setValueToGuess(value: String){
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

    private fun updateLastRecord(leaderboardItem: LeaderboardItem){
        viewModelScope.launch {
            repository.updateLastRecord(leaderboardItem)
        }
    }

    private fun saveRecord(leaderboardItem: LeaderboardItem){
        viewModelScope.launch {
            repository.saveRecord(leaderboardItem, _screenState.value.selectedType)
        }
    }

    fun limitRecordsList(limit: Int = 20){
        val currentLeaderboard = when(_screenState.value.selectedType){
            QuizType.GuessRoman -> _screenState.value.romanLeaderboard
            QuizType.GuessArabic -> _screenState.value.romanLeaderboard
            QuizType.GuessBoth -> _screenState.value.romanLeaderboard
        }
        if(currentLeaderboard.size > limit){
            val out = currentLeaderboard.subList(limit, currentLeaderboard.size)
            out.forEach {
                deleteRecord(it)
            }
        }
    }

    fun restoreRecords(){
        viewModelScope.launch {
            repository.saveRecords(_screenState.value.lastDeletedItems, _screenState.value.selectedType)
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


    fun nextValueToGuess(): String {
        setInputValue("")
        if(_screenState.value.selectedType == QuizType.GuessBoth) {
            setCurrentQuizType(
                if (Random.nextInt(2) == 1) QuizType.GuessRoman
                else QuizType.GuessArabic
            )
        }
        val type = _screenState.value.currentQuizType
        val count = _screenState.value.currentCount

        val step = _screenState.value.levelStep
        val numberRange = when(count){
            in 0        until step * 1 -> 1..39
            in step * 1 until step * 2 -> 40..89
            in step * 2 until step * 3 -> 90..399
            in step * 3 until step * 4 -> 400..899
            in step * 4 until step * 5 -> 900..3999
            in step * 5 until step * 6 -> 4000..8999
            in step * 6 until step * 7 -> 9000..39999
            in step * 7 until step * 8 -> 40000..89999
            in step * 8 until step * 9 -> 90000..399999
            in step * 9 until step * 10 -> 400000..899999
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
                currentTime = 30,
                currentInputValue = ""
            )
        }
        setValueToGuess(nextValueToGuess())
        saveRecord(LeaderboardItem(count = 0, score = 0))
        time = _screenState.value.currentTime
    }

    fun successfulGuess() {
        val newTime = _screenState.value.currentTime
        val deltaTime = time - newTime

        val timeToAdd: Int
        val minScore: Int
        val maxScore: Int
        val step = _screenState.value.levelStep
        when(_screenState.value.currentCount){
            in  0       until step * 1 -> {
                timeToAdd = 5
                minScore = 25
                maxScore = 50
            }
            in step * 1 until step * 2 -> {
                timeToAdd = 5
                minScore = 50
                maxScore = 100
            }
            in step * 2 until step * 3 -> {
                timeToAdd = 5
                minScore = 75
                maxScore = 150
            }
            in step * 3 until step * 4 -> {
                timeToAdd = 10
                minScore = 100
                maxScore = 200
            }
            in step * 4 until step * 5 -> {
                timeToAdd = 10
                minScore = 125
                maxScore = 250
            }
            in step * 5 until step * 6 -> {
                timeToAdd = 10
                minScore = 150
                maxScore = 300
            }
            in step * 6 until step * 7 -> {
                timeToAdd = 10
                minScore = 175
                maxScore = 350
            }
            in step * 7 until step * 8 -> {
                timeToAdd = 15
                minScore = 200
                maxScore = 400
            }
            in step * 8 until step * 9 -> {
                timeToAdd = 15
                minScore = 225
                maxScore = 450
            }
            in step * 9 until step * 10 -> {
                timeToAdd = 20
                minScore = 250
                maxScore = 500
            }
            else -> {
                timeToAdd = 15
                minScore = 275
                maxScore = 550
            }
        }
        val scoreToAdd = if(deltaTime > timeToAdd) minScore
                         else mapRange(deltaTime, 0, timeToAdd, maxScore, minScore)

        incrementGameTimer(timeToAdd)
        incrementScore(scoreToAdd)
        incrementCurrentCount()
        updateLastRecord(
            LeaderboardItem(
                count = _screenState.value.currentCount,
                score = _screenState.value.currentScore
            )
        )

        time = _screenState.value.currentTime
    }

    fun quizOver() {
        updateLastRecord(
            LeaderboardItem(
                count = _screenState.value.currentCount,
                score = _screenState.value.currentScore
            )
        )
    }

    //AD logic
    private var mInterstitialAd: InterstitialAd? = null
    fun loadInterstitialAd(context: Context, adID: String = ""){
        val testID = "ca-app-pub-3940256099942544/1033173712"
        InterstitialAd.load(
            context,
            //testID,
            adID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let {
                        Log.d("puk", it)
                        //Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d("puk", "Ad was loaded.")
                    //Toast.makeText(context, "Ad was loaded.", Toast.LENGTH_SHORT).show()
                    mInterstitialAd = interstitialAd
                }
            }
        )
    }
    fun showInterstitialAd(context: Context){
        val egg: Int?
        runBlocking {
            egg = context.intSettings["egg"].first()
        }
        if(egg != 1){
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(context as Activity)
                loadInterstitialAd(context)
            }
            else {
                Log.d("puk", "The interstitial ad wasn't ready yet.")
            }
        }
    }

    private var mRewardedAd: RewardedAd? = null
    fun loadRewardedAd(context: Context, adID: String = ""){
        val testID = "ca-app-pub-3940256099942544/5224354917"
        RewardedAd.load(
            context,
            //testID,
            adID,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let {
                        Log.d("puk", it)
                        //Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                    Log.d("puk", "Ad was loaded.")
                    //Toast.makeText(context, "Ad was loaded.", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    fun isRewardedAdLoaded(): Boolean = mRewardedAd != null

    fun showRewardedAd(context: Context, onRewardReceived: () -> Unit){
        mRewardedAd?.let { ad ->
            mRewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    onRewardReceived()
                    mRewardedAd = null
                    loadRewardedAd(context)
                }
            }
            ad.show(context as  Activity) { _ ->
                Log.d("puk", "User earned the reward.")
                //Toast.makeText(context, "User earned the reward.", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Log.d("puk", "The rewarded ad wasn't ready yet.")
            //Toast.makeText(context, "The rewarded ad wasn't ready yet", Toast.LENGTH_SHORT).show()
        }
    }



    fun isNewRecordSet(): Boolean {
        val record = _screenState.value.currentScore
        val currentLeaderboard = when(_screenState.value.selectedType){
            QuizType.GuessRoman -> _screenState.value.romanLeaderboard
            QuizType.GuessArabic -> _screenState.value.arabicLeaderboard
            QuizType.GuessBoth -> _screenState.value.bothLeaderboard
        }

        return if(currentLeaderboard.size >= 2) record > currentLeaderboard[1].score
               else true
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