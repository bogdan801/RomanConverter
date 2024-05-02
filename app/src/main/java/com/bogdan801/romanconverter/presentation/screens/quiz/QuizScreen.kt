package com.bogdan801.romanconverter.presentation.screens.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.data.util.convertArabicToRoman
import com.bogdan801.romanconverter.data.util.convertRomanToArabic
import com.bogdan801.romanconverter.domain.model.QuizType
import com.bogdan801.romanconverter.presentation.components.ActionButton
import com.bogdan801.romanconverter.presentation.components.AutoSizeText
import com.bogdan801.romanconverter.presentation.components.InputKeyboard
import com.bogdan801.romanconverter.presentation.components.InputKeyboardType
import com.bogdan801.romanconverter.presentation.components.SmallIconButton
import com.bogdan801.romanconverter.presentation.navigation.Screen
import com.bogdan801.romanconverter.presentation.screens.quiz.components.DeleteConfirmDialogBox
import com.bogdan801.romanconverter.presentation.screens.quiz.components.LeaderboardItemRow
import com.bogdan801.romanconverter.presentation.screens.quiz.components.QuizTypeSelector
import com.bogdan801.romanconverter.presentation.screens.home.HomeViewModel
import com.bogdan801.romanconverter.presentation.screens.quiz.components.PauseDialogBox
import com.bogdan801.romanconverter.presentation.screens.quiz.components.QuizDisplay
import com.bogdan801.romanconverter.presentation.screens.quiz.components.QuizOverDialogBox
import com.bogdan801.util_library.intSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizScreen(
    navController: NavHostController,
    viewModel: QuizViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val homeScreenState by homeViewModel.screenState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val soundOn by context.intSettings["sound_on"].collectAsStateWithLifecycle(initialValue = 1)

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        modifier = Modifier
                            .padding(4.dp)
                            .offset(y = (-8).dp),
                        action = {
                            data.visuals.actionLabel?.let {
                                TextButton(
                                    onClick = {
                                        viewModel.restoreRecords()
                                        snackbarHostState.currentSnackbarData?.dismiss()
                                    }
                                ) {
                                    Text(
                                        text = data.visuals.actionLabel!!,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.onTertiary,
                        contentColor = MaterialTheme.colorScheme.background
                    ) {
                        Text(
                            text = data.visuals.message,
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 14.sp
                        )
                    }
                }
            )
        },
        containerColor = Color.Transparent
    ) { defaultPadding ->
        AnimatedContent(
            targetState = screenState.isQuizStarted,
            label = ""
        ) { isQuizStarted ->
            if(!isQuizStarted){
                Box(modifier = Modifier.fillMaxSize()){
                    if(navController.currentDestination?.route == Screen.Quiz.route) {
                        SmallIconButton(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp),
                            onClick = {
                                scope.launch {
                                    if(soundOn != 1) context.intSettings.set("sound_on", 1)
                                    else context.intSettings.set("sound_on", 0)
                                }
                            }
                        ){
                            val primary = MaterialTheme.colorScheme.primary
                            val secondary = MaterialTheme.colorScheme.secondary
                            Icon(
                                modifier = Modifier
                                    .graphicsLayer(alpha = 0.99f)
                                    .drawWithCache {
                                        onDrawWithContent {
                                            drawContent()
                                            drawRect(
                                                brush = Brush.verticalGradient(
                                                    colors = listOf(
                                                        primary, secondary, primary
                                                    )
                                                ),
                                                blendMode = BlendMode.SrcAtop
                                            )
                                        }
                                    },
                                imageVector = if(soundOn != 0) Icons.AutoMirrored.Default.VolumeOff
                                else Icons.AutoMirrored.Default.VolumeUp,
                                contentDescription = "Sound On Switch",
                            )
                        }
                    }
                    var weightOfSelector by remember { mutableFloatStateOf(0.8f) }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .onGloballyPositioned {
                                val screenAspectRatio = it.size.width.toFloat() / it.size.height
                                weightOfSelector = if (screenAspectRatio > 0.55f) 0.35f else 0.8f
                            }
                            .padding(defaultPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(54.dp))
                        AutoSizeText(
                            modifier = Modifier.padding(horizontal = 72.dp),
                            text = "Choose the quiz type",
                            maxLines = 1,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onTertiary,
                            maxTextSize = 22.sp,
                            minTextSize = MaterialTheme.typography.titleMedium.fontSize,
                            textAlign = TextAlign.Center
                        )
                        QuizTypeSelector(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, end = 24.dp, top = 16.dp)
                                .weight(weightOfSelector),
                            selectedType = screenState.selectedType,
                            onTypeSelected = { type ->
                                viewModel.setType(type)
                                snackbarHostState.currentSnackbarData?.dismiss()
                            }
                        )
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            val width = maxWidth
                            val height = maxHeight
                            var showDeleteDialogBox by remember { mutableStateOf(false) }
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .padding(top = 12.dp)
                                        .height(height * 0.25f),
                                    painter = painterResource(
                                        id = R.drawable.ornament_leaderboard_titlebox
                                    ),
                                    contentDescription = "Leaderboard",
                                    tint = MaterialTheme.colorScheme.onTertiary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                BoxWithConstraints(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    val itemHeight = if (maxHeight > 336.dp) 48.dp else 36.dp
                                    AnimatedContent(
                                        targetState = screenState.selectedType,
                                        label = "",
                                        transitionSpec = {
                                            slideInVertically(initialOffsetY = { -it }) togetherWith
                                                    slideOutVertically(targetOffsetY = { it })
                                        }
                                    ) { type ->
                                        LazyColumn(
                                            modifier = Modifier
                                                .padding(horizontal = 26.dp)
                                                .fillMaxSize()
                                                .background(MaterialTheme.colorScheme.background)
                                                .border(
                                                    width = 1.dp,
                                                    color = MaterialTheme
                                                        .colorScheme
                                                        .outlineVariant
                                                        .copy(alpha = 0.33f)
                                                )
                                        ) {
                                            itemsIndexed(
                                                items = when (type) {
                                                    QuizType.GuessRoman -> screenState.romanLeaderboard
                                                    QuizType.GuessArabic -> screenState.arabicLeaderboard
                                                    QuizType.GuessBoth -> screenState.bothLeaderboard
                                                },
                                                key = { _, item ->
                                                    item.id
                                                }
                                            ) { id, item ->
                                                LeaderboardItemRow(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(itemHeight)
                                                        .animateItemPlacement(),
                                                    position = id + 1,
                                                    data = item,
                                                    onDeleteClick = {
                                                        viewModel.deleteRecord(item)
                                                        scope.launch {
                                                            snackbarHostState.currentSnackbarData?.dismiss()
                                                            snackbarHostState.showSnackbar(
                                                                message = "The record has been deleted",
                                                                actionLabel = "RESTORE",
                                                                duration = SnackbarDuration.Short
                                                            )
                                                        }
                                                    },
                                                    onDeleteAllClick = {
                                                        showDeleteDialogBox = true
                                                    }
                                                )
                                            }
                                        }
                                        val isListEmpty = when (type) {
                                            QuizType.GuessRoman -> screenState.romanLeaderboard.isEmpty()
                                            QuizType.GuessArabic -> screenState.arabicLeaderboard.isEmpty()
                                            QuizType.GuessBoth -> screenState.bothLeaderboard.isEmpty()
                                        }
                                        if(isListEmpty){
                                            Box(modifier = Modifier.fillMaxSize()){
                                                Text(
                                                    modifier = Modifier.align(Alignment.Center),
                                                    text = "The list is empty",
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    textAlign = TextAlign.Center,
                                                    color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.5f)
                                                )
                                            }
                                        }
                                    }
                                    Icon(
                                        modifier = Modifier
                                            .align(Alignment.TopCenter)
                                            .padding(horizontal = 12.dp)
                                            .fillMaxWidth()
                                            .aspectRatio(22f)
                                            .offset(y = -((width - 24.dp) / 48f)),
                                        painter = painterResource(
                                            id = R.drawable.ornament_leaderbard_list
                                        ),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.onTertiary
                                    )
                                    Icon(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(horizontal = 12.dp)
                                            .fillMaxWidth()
                                            .aspectRatio(22f)
                                            .offset(y = (width - 24.dp) / 48f),
                                        painter = painterResource(
                                            id = R.drawable.ornament_leaderbard_list
                                        ),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.onTertiary
                                    )
                                }
                            }
                            DeleteConfirmDialogBox (
                                show = showDeleteDialogBox,
                                onVisibilityChanged = {
                                    homeViewModel.blurBackground(it)
                                },
                                onCancelClick = {
                                    showDeleteDialogBox = false
                                },
                                onConfirmClick = {
                                    viewModel.clearLeaderboard()
                                    showDeleteDialogBox = false
                                    scope.launch {
                                        snackbarHostState.currentSnackbarData?.dismiss()
                                        snackbarHostState.showSnackbar(
                                            message = "All records have been deleted",
                                            actionLabel = "RESTORE",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        ActionButton(
                            label = "START THE QUIZ",
                            onClick = {
                                viewModel.startQuiz(homeViewModel)

                                /*val score = Random.nextInt(10000, 25000)
                                viewModel.saveRecord(
                                    LeaderboardItem(
                                        id = Random.nextInt(0, 100000),
                                        date = LocalDate.now().minusDays(Random.nextLong(0, 365)),
                                        score = score,
                                        count = score / 800
                                    )
                                )*/
                                /*scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(message = "Quiz has started", duration = SnackbarDuration.Short)
                                }*/
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

            }
            else {
                var isPaused by rememberSaveable { mutableStateOf(false) }
                var isQuizOver by rememberSaveable { mutableStateOf(false) }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    SmallIconButton(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp),
                        onClick = {
                            isPaused = true
                        }
                    ){
                        val primary = MaterialTheme.colorScheme.primary
                        val secondary = MaterialTheme.colorScheme.secondary
                        Icon(
                            modifier = Modifier
                                .graphicsLayer(alpha = 0.99f)
                                .drawWithCache {
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(
                                            brush = Brush.verticalGradient(
                                                colors = listOf(
                                                    primary, secondary, primary
                                                )
                                            ),
                                            blendMode = BlendMode.SrcAtop
                                        )
                                    }
                                },
                            imageVector = Icons.Default.Pause,
                            contentDescription = "Sound On Switch",
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.height(56.dp))
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                            contentAlignment = Alignment.Center
                        ){
                            val titleType = when(screenState.currentQuizType){
                                QuizType.GuessRoman -> "Arabic"
                                QuizType.GuessArabic -> "Roman"
                                QuizType.GuessBoth -> ""
                            }
                            AutoSizeText(
                                modifier = Modifier.padding(horizontal = 72.dp),
                                text = "Type the number using $titleType numerals",
                                maxLines = 2,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onTertiary,
                                maxTextSize = 22.sp,
                                minTextSize = MaterialTheme.typography.titleMedium.fontSize,
                                textAlign = TextAlign.Center
                            )
                        }


                        var isKeyboardActive by rememberSaveable { mutableStateOf(true) }
                        var showSuccessfulGuessIcon by rememberSaveable { mutableStateOf(false) }
                        //start timer
                        var startTimer by rememberSaveable { mutableIntStateOf(3) }
                        LaunchedEffect(key1 = isPaused, key2 = isQuizOver) {
                            if(!isPaused && !isQuizOver){
                                isKeyboardActive = false
                                while (startTimer > 0){
                                    delay(1000)
                                    startTimer--
                                }
                                isKeyboardActive = true
                            }
                        }
                        //game timer
                        LaunchedEffect(key1 = isPaused, key2 = startTimer, key3 = showSuccessfulGuessIcon) {
                            if(!isPaused && startTimer == 0 && !showSuccessfulGuessIcon){
                                while (screenState.currentTime > 0){
                                    viewModel.setGameTimer(screenState.currentTime - 1)
                                    delay(1000)
                                }
                                if(screenState.currentTime == 0){
                                    viewModel.quizOver()
                                    isQuizOver = true
                                    isKeyboardActive = false
                                }
                            }
                        }
                        //check if guess successful
                        LaunchedEffect(key1 = screenState.currentInputValue) {
                            suspend fun onSuccess() {
                                showSuccessfulGuessIcon = true
                                delay(1000)
                                viewModel.successfulGuess()
                                showSuccessfulGuessIcon = false
                            }

                            val inputValue = screenState.currentInputValue
                            val numberToGuess = screenState.currentValueToGuess
                            when(screenState.currentQuizType){
                                QuizType.GuessRoman -> {
                                    if(convertArabicToRoman(inputValue) == numberToGuess) onSuccess()
                                }
                                QuizType.GuessArabic -> {
                                    if(convertRomanToArabic(inputValue) == numberToGuess) onSuccess()
                                }
                                QuizType.GuessBoth -> {}
                            }
                        }


                        QuizDisplay(
                            modifier = Modifier.fillMaxWidth(),
                            startTimer = startTimer,
                            numberToGuess = screenState.currentValueToGuess,
                            inputValue = screenState.currentInputValue,
                            count = screenState.currentCount,
                            time = screenState.currentTime,
                            score = screenState.currentScore,
                            hideNumber = isPaused,
                            showSuccessfulGuessIcon = showSuccessfulGuessIcon
                        )

                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f),
                            contentAlignment = Alignment.Center
                        ){
                            InputKeyboard(
                                romanValue = screenState.currentInputValue,
                                arabicValue = screenState.currentInputValue,
                                onRomanValueChange = {
                                    viewModel.setInputValue(it)
                                },
                                onArabicValueChange = {
                                    viewModel.setInputValue(it)
                                },
                                type = when(screenState.currentQuizType){
                                    QuizType.GuessRoman -> InputKeyboardType.Arabic
                                    QuizType.GuessArabic -> InputKeyboardType.Roman
                                    else -> InputKeyboardType.Roman
                                },
                                onClear = {
                                    viewModel.setInputValue("")
                                },
                                isQuizInput = true,
                                isActive = isKeyboardActive
                            )
                        }
                        PauseDialogBox(
                            show = isPaused,
                            onVisibilityChanged = {
                                homeViewModel.blurBackground(it)
                            },
                            onDismiss = {
                                isPaused = false
                            },
                            onHomeClick = {
                                viewModel.stopQuiz(homeViewModel)
                            }
                        )
                        QuizOverDialogBox(
                            show = isQuizOver,
                            onVisibilityChanged = {
                                homeViewModel.blurBackground(it)
                            },
                            onDismiss = {
                                viewModel.stopQuiz(homeViewModel)
                            },
                            onTryAgainClick = {
                                viewModel.setupTheQuiz()
                                startTimer = 3
                                isQuizOver = false
                            },
                            score = screenState.currentScore,
                            count = screenState.currentCount
                        )
                    }

                }
                BackHandler {
                    viewModel.stopQuiz(homeViewModel)
                }
            }
        }
    }
}