package com.bogdan801.romanconverter.presentation.screens.quiz

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.data.util.convertArabicToRoman
import com.bogdan801.romanconverter.domain.model.LeaderboardData
import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import com.bogdan801.romanconverter.domain.model.QuizType
import com.bogdan801.romanconverter.domain.model.RecordItem
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
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.leaderboard.LeaderboardVariant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    //records logic
    private fun setupRomanLeaderboard(list: List<RecordItem>){
        _screenState.update {
            it.copy(
                romanRecords = list
            )
        }
    }

    private fun setupArabicLeaderboard(list: List<RecordItem>){
        _screenState.update {
            it.copy(
                arabicRecords = list
            )
        }
    }

    private fun setupBothLeaderboard(list: List<RecordItem>){
        _screenState.update {
            it.copy(
                bothRecords = list
            )
        }
    }

    private fun updateLastRecord(recordItem: RecordItem){
        viewModelScope.launch {
            repository.updateLastRecord(recordItem)
        }
    }

    private fun saveRecord(recordItem: RecordItem){
        viewModelScope.launch {
            repository.saveRecord(recordItem, _screenState.value.selectedType)
        }
    }

    fun limitRecordsList(limit: Int = 20){
        val currentLeaderboard = when(_screenState.value.selectedType){
            QuizType.GuessRoman -> _screenState.value.romanRecords
            QuizType.GuessArabic -> _screenState.value.romanRecords
            QuizType.GuessBoth -> _screenState.value.romanRecords
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

    private fun setLastDeletedRecords(items: List<RecordItem>){
        _screenState.update {
            it.copy(
                lastDeletedItems = items
            )
        }
    }

    fun deleteRecord(recordItem: RecordItem){
        setLastDeletedRecords(listOf(recordItem))
        viewModelScope.launch {
            repository.deleteRecord(recordItem.id, _screenState.value.selectedType)
        }
    }

    fun clearLeaderboard(){
        setLastDeletedRecords(
            when(_screenState.value.selectedType){
                QuizType.GuessRoman -> _screenState.value.romanRecords
                QuizType.GuessArabic -> _screenState.value.arabicRecords
                QuizType.GuessBoth -> _screenState.value.bothRecords
            }
        )
        viewModelScope.launch {
            repository.clearRecordsOfAType(_screenState.value.selectedType)
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

    fun stopQuiz(homeViewModel: HomeViewModel, activity: Activity) {
        _screenState.update {
            it.copy(
                isQuizStarted = false
            )
        }
        homeViewModel.showNavBar(true)
        synchronizeTopRecordWithLeaderboard(activity)
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
        saveRecord(RecordItem(count = 0, score = 0))
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
            RecordItem(
                count = _screenState.value.currentCount,
                score = _screenState.value.currentScore
            )
        )

        time = _screenState.value.currentTime
    }

    fun quizOver() {
        updateLastRecord(
            RecordItem(
                count = _screenState.value.currentCount,
                score = _screenState.value.currentScore
            )
        )
    }

    //AD logic
    private var mInterstitialAd: InterstitialAd? = null
    fun loadInterstitialAd(context: Context, adID: String = "ca-app-pub-7610563481346603/3989723927"){
        //val testID = "ca-app-pub-3940256099942544/1033173712"
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
    fun loadRewardedAd(context: Context, adID: String = "ca-app-pub-7610563481346603/2245397419"){
        //val testID = "ca-app-pub-3940256099942544/5224354917"
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
        val currentRecords = when(_screenState.value.selectedType){
            QuizType.GuessRoman -> _screenState.value.romanRecords
            QuizType.GuessArabic -> _screenState.value.arabicRecords
            QuizType.GuessBoth -> _screenState.value.bothRecords
        }

        return if(currentRecords.size >= 2) record > currentRecords[1].score
        else true
    }


    //leaderboard logic
    fun initializeGamesServices(activity: Activity) {
        val gamesSignInClient = PlayGames.getGamesSignInClient(activity)

        gamesSignInClient.isAuthenticated()
            .addOnCompleteListener { isAuthenticatedTask ->
                val isAuthenticated =
                    (isAuthenticatedTask.isSuccessful && isAuthenticatedTask.result.isAuthenticated)

                if(isAuthenticated){
                    setIsLoggedIn(true)
                    setUpLeaderboards(activity)
                }
                else {
                    setIsLoggedIn(false)
                }
            }
    }

    fun logInToPlayServices(activity: Activity) {
        val gamesSignInClient = PlayGames.getGamesSignInClient(activity)
        gamesSignInClient.signIn().addOnCompleteListener { isAuthenticatedTask ->
            val isAuthenticated =
                (isAuthenticatedTask.isSuccessful && isAuthenticatedTask.result.isAuthenticated)

            if(isAuthenticated){
                setIsLoggedIn(true)
                setUpLeaderboards(activity)
            }
            else {
                setIsLoggedIn(false)
                Toast.makeText(
                    activity,
                    activity.getString(R.string.quiz_login_failed),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun setUpLeaderboards(activity: Activity) {
        viewModelScope.launch {
            //getting userID
            PlayGames.getPlayersClient(activity).currentPlayer.addOnCompleteListener { mTask ->
                setUserID(mTask.result.playerId)
            }
            //getting leaderboard data
            launch {
                getLeaderboardDataForAType(QuizType.GuessRoman, activity)
            }

            launch {
                getLeaderboardDataForAType(QuizType.GuessArabic, activity)
            }

            launch {
                getLeaderboardDataForAType(QuizType.GuessBoth, activity)
            }
        }
    }

    private fun getLeaderboardDataForAType(type: QuizType, activity: Activity, count: Int = 20) {
        if(_screenState.value.isUserLoggedIn){
            val leaderboardClient = PlayGames.getLeaderboardsClient(activity)

            val leaderboardID = when(type){
                QuizType.GuessRoman -> activity.getString(R.string.leaderboard_id_roman)
                QuizType.GuessArabic -> activity.getString(R.string.leaderboard_id_arabic)
                QuizType.GuessBoth -> activity.getString(R.string.leaderboard_id_both)
            }

            setIsLeaderboardLoading(true, type)

            //getting to scores
            leaderboardClient.loadTopScores(
                leaderboardID,
                LeaderboardVariant.TIME_SPAN_ALL_TIME,
                LeaderboardVariant.COLLECTION_PUBLIC,
                count
            ).addOnCompleteListener { task ->
                val playersList: MutableList<LeaderboardItem>?
                //if data received successfully
                if(task.isSuccessful){
                    playersList = mutableListOf()
                    task.result.get()?.scores?.forEach { score ->
                        playersList.add(
                            LeaderboardItem(
                                rank = score.rank.toInt(),
                                username = score.scoreHolderDisplayName,
                                score = score.rawScore.toInt(),
                                isUser = score.scoreHolder!!.playerId == _screenState.value.userID
                            )
                        )
                    }

                    //if list doesn't contain player and is not empty
                    if(playersList.isNotEmpty() && !playersList.any { it.isUser }){
                        leaderboardClient.loadCurrentPlayerLeaderboardScore(
                            leaderboardID,
                            LeaderboardVariant.TIME_SPAN_ALL_TIME,
                            LeaderboardVariant.COLLECTION_PUBLIC
                        ).addOnCompleteListener { getPlayerTask ->
                            if(getPlayerTask.isSuccessful){
                                getPlayerTask.result.get()?.let {
                                    val playerData = LeaderboardItem(
                                        rank = it.rank.toInt(),
                                        username = it.scoreHolderDisplayName,
                                        score = it.rawScore.toInt(),
                                        isUser = true
                                    )
                                    playersList.add(playerData)
                                }
                            }

                            val output = LeaderboardData(
                                records = playersList
                            )
                            when(type){
                                QuizType.GuessRoman -> setRomanLeaderboard(output)
                                QuizType.GuessArabic -> setArabicLeaderboard(output)
                                QuizType.GuessBoth -> setBothLeaderboard(output)
                            }
                        }
                    }
                    //if player is in the list or list is empty
                    else {
                        val output = LeaderboardData(
                            records = playersList
                        )
                        when(type){
                            QuizType.GuessRoman -> setRomanLeaderboard(output)
                            QuizType.GuessArabic -> setArabicLeaderboard(output)
                            QuizType.GuessBoth -> setBothLeaderboard(output)
                        }
                    }
                }
                //if error while loading
                else {
                    val output = LeaderboardData(
                        error = "Could not load Leaderboard data. Check your connection and try again"
                    )
                    when(type){
                        QuizType.GuessRoman -> setRomanLeaderboard(output)
                        QuizType.GuessArabic -> setArabicLeaderboard(output)
                        QuizType.GuessBoth -> setBothLeaderboard(output)
                    }
                }
                setIsLeaderboardLoading(false, type)
            }
        }
    }

    private fun submitRecordToLeaderboard(activity: Activity, score: Int, type: QuizType = _screenState.value.currentQuizType){
        if(_screenState.value.isUserLoggedIn && score > 40){
            setIsLeaderboardLoading(true, type)
            viewModelScope.launch {
                val leaderboardClient = PlayGames.getLeaderboardsClient(activity)

                val leaderboardID = when(type){
                    QuizType.GuessRoman -> activity.getString(R.string.leaderboard_id_roman)
                    QuizType.GuessArabic -> activity.getString(R.string.leaderboard_id_arabic)
                    QuizType.GuessBoth -> activity.getString(R.string.leaderboard_id_both)
                }

                leaderboardClient.submitScoreImmediate(
                    leaderboardID,
                    score.toLong()
                ).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        viewModelScope.launch {
                            delay(3000)
                            getLeaderboardDataForAType(type, activity)
                        }
                    }
                    else {
                        Toast.makeText(
                            activity,
                            "Error while submitting score. Check your internet connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    setIsLeaderboardLoading(false, type)
                }
            }
        }
    }

    fun synchronizeTopRecordWithLeaderboard(activity: Activity) {
        if(_screenState.value.isUserLoggedIn){
            viewModelScope.launch {
                launch {
                    synchronizeByType(activity, QuizType.GuessRoman)
                }

                launch {
                    synchronizeByType(activity, QuizType.GuessArabic)
                }

                launch {
                    synchronizeByType(activity, QuizType.GuessBoth)
                }
            }
        }
    }

    private fun synchronizeByType(activity: Activity, type: QuizType){
        val records = when(type){
            QuizType.GuessRoman -> _screenState.value.romanRecords
            QuizType.GuessArabic -> _screenState.value.arabicRecords
            QuizType.GuessBoth -> _screenState.value.bothRecords
        }

        val leaderboard = when(type){
            QuizType.GuessRoman -> _screenState.value.romanLeaderboard
            QuizType.GuessArabic -> _screenState.value.arabicLeaderboard
            QuizType.GuessBoth -> _screenState.value.bothLeaderboard
        }

        if(leaderboard.records != null && records.isNotEmpty()){
            if(leaderboard.records.any { it.isUser }){
                val userLeaderboardScore = leaderboard.records.find { it.isUser }!!.score
                val userTopRecordScore = records.first().score
                if(userTopRecordScore > userLeaderboardScore) {
                    submitRecordToLeaderboard(activity, userTopRecordScore, type)
                }
            }
            else {
                val userTopRecordScore = records.first().score
                submitRecordToLeaderboard(activity, userTopRecordScore, type)
            }
        }
    }

    fun showLeaderboard(activity: Activity, type: QuizType) {
        val id = when(type){
            QuizType.GuessRoman -> R.string.leaderboard_id_roman
            QuizType.GuessArabic -> R.string.leaderboard_id_arabic
            QuizType.GuessBoth -> R.string.leaderboard_id_both
        }

        PlayGames.getLeaderboardsClient(activity)
            .getLeaderboardIntent(activity.getString(id))
            .addOnSuccessListener { intent ->
                activity.startActivityForResult(intent, 9004)
            }
    }

    private fun setIsLoggedIn(isLoggedIn: Boolean){
        _screenState.update {
            it.copy(
                isUserLoggedIn = isLoggedIn
            )
        }
    }

    private fun setIsLeaderboardLoading(isLoading: Boolean, type: QuizType){
        when(type){
            QuizType.GuessRoman -> {
                _screenState.update {
                    it.copy(
                        romanLeaderboardLoading = isLoading
                    )
                }
            }
            QuizType.GuessArabic -> {
                _screenState.update {
                    it.copy(
                        arabicLeaderboardLoading = isLoading
                    )
                }
            }
            QuizType.GuessBoth -> {
                _screenState.update {
                    it.copy(
                        bothLeaderboardLoading = isLoading
                    )
                }
            }
        }

    }

    private fun setUserID(userID: String){
        _screenState.update {
            it.copy(
                userID = userID
            )
        }
    }

    private fun setRomanLeaderboard(leaderboardData: LeaderboardData){
        _screenState.update {
            it.copy(
                romanLeaderboard = leaderboardData
            )
        }
    }

    private fun setArabicLeaderboard(leaderboardData: LeaderboardData){
        _screenState.update {
            it.copy(
                arabicLeaderboard = leaderboardData
            )
        }
    }

    private fun setBothLeaderboard(leaderboardData: LeaderboardData){
        _screenState.update {
            it.copy(
                bothLeaderboard = leaderboardData
            )
        }
    }



    init {
        viewModelScope.launch {
            repository.getRomanRecordsFlow().collect{
                setupRomanLeaderboard(it)
            }
        }
        viewModelScope.launch {
            repository.getArabicRecordsFlow().collect{
                setupArabicLeaderboard(it)
            }
        }
        viewModelScope.launch {
            repository.getBothRecordsFlow().collect{
                setupBothLeaderboard(it)
            }
        }
    }
}