package com.bogdan801.romanconverter.presentation.screens.quiz

import android.app.Activity
import android.media.MediaPlayer
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
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
import com.bogdan801.romanconverter.presentation.screens.home.HomeViewModel
import com.bogdan801.romanconverter.presentation.screens.quiz.components.DeleteConfirmDialogBox
import com.bogdan801.romanconverter.presentation.screens.quiz.components.LeaderboardList
import com.bogdan801.romanconverter.presentation.screens.quiz.components.RecordsItemRow
import com.bogdan801.romanconverter.presentation.screens.quiz.components.LeaderboardTitle
import com.bogdan801.romanconverter.presentation.screens.quiz.components.PageIndicator
import com.bogdan801.romanconverter.presentation.screens.quiz.components.PauseDialogBox
import com.bogdan801.romanconverter.presentation.screens.quiz.components.QuizDisplay
import com.bogdan801.romanconverter.presentation.screens.quiz.components.QuizOverDialogBox
import com.bogdan801.romanconverter.presentation.screens.quiz.components.QuizTypeSelector
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    //sound on setting
    val soundOn by context.intSettings["sound_on"].collectAsStateWithLifecycle(initialValue = null)

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            //snack bar for restoring deleted items
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
        LaunchedEffect(key1 = true) {
            viewModel.initializeGamesServices(context as Activity)
        }
        LaunchedEffect(
            key1 = screenState.romanLeaderboard,
            key2 = screenState.arabicLeaderboard,
            key3 = screenState.bothLeaderboard,
        ) {
            if(
                screenState.romanLeaderboard.records != null &&
                screenState.arabicLeaderboard.records != null &&
                screenState.bothLeaderboard.records != null
            ) {
                //sync first launch
                viewModel.synchronizeTopRecordWithLeaderboard(context as Activity)
            }
        }

        AnimatedContent(
            targetState = screenState.isQuizStarted,
            label = ""
        ) { isQuizStarted ->
            //leaderboard screen
            if(!isQuizStarted){
                Box(modifier = Modifier.fillMaxSize()){
                    //sound control button
                    if(navController.currentDestination?.route == Screen.Quiz.route) {
                        SmallIconButton(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp),
                            onClick = {
                                scope.launch {
                                    if(soundOn == 0) context.intSettings.set("sound_on", 1)
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
                                imageVector = if(soundOn == null || soundOn == 1) {
                                    Icons.AutoMirrored.Default.VolumeOff
                                }
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
                            text = stringResource(id = R.string.quiz_title),
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

                        //leaderboard panel
                        val pagerState = rememberPagerState(pageCount = { 2 })
                        HorizontalPager(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            state = pagerState
                        ) { page ->
                            //leaderboard
                            if(page == 0){
                                BoxWithConstraints(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    val width = maxWidth
                                    val height = maxHeight
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        //leaderboard title
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceEvenly,
                                        ) {
                                            SmallIconButton(
                                                modifier = Modifier
                                                    .offset(y = (height * 0.115f)),
                                                isVisible = screenState.isUserLoggedIn,
                                                onClick = {
                                                    viewModel.showLeaderboard(
                                                        context as Activity,
                                                        screenState.selectedType
                                                    )
                                                },
                                                icon = {
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
                                                        imageVector = Icons.Default.Leaderboard,
                                                        contentDescription = "Show leaderboard",
                                                    )
                                                }
                                            )
                                            LeaderboardTitle(
                                                modifier = Modifier
                                                    .padding(top = 12.dp),
                                                frameHeight = height * 0.25f,
                                                title = stringResource(
                                                    id = R.string.quiz_leaderboard
                                                )
                                            )
                                            Box(modifier = Modifier.size(42.dp))
                                        }

                                        Spacer(modifier = Modifier.height(2.dp))
                                        //leaderboard list
                                        BoxWithConstraints(
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            val itemHeight = if (maxHeight > 336.dp) 48.dp else 36.dp
                                            Box(modifier = Modifier
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
                                            ){
                                                if(!screenState.isUserLoggedIn){
                                                    Column(
                                                        modifier = Modifier.align(Alignment.Center),
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.spacedBy(24.dp)
                                                    ) {
                                                        Text(
                                                            text = stringResource(
                                                                id = R.string.quiz_log_in
                                                            ),
                                                            color = MaterialTheme.colorScheme.onTertiary,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            textAlign = TextAlign.Center
                                                        )
                                                        ActionButton(
                                                            size = DpSize(120.dp, 46.dp),
                                                            label = stringResource(
                                                                id = R.string.quiz_log_in_title
                                                            ),
                                                            onClick = {
                                                                viewModel.logInToPlayServices(context as Activity)
                                                            }
                                                        )
                                                    }
                                                }
                                                else{
                                                    AnimatedContent(
                                                        modifier = Modifier.fillMaxSize(),
                                                        targetState = screenState.selectedType,
                                                        label = "",
                                                        transitionSpec = {
                                                            slideInVertically(initialOffsetY = { -it }) togetherWith
                                                                    slideOutVertically(targetOffsetY = { it })
                                                        }
                                                    ) { type ->
                                                        val data = when(type){
                                                            QuizType.GuessRoman -> screenState.romanLeaderboard
                                                            QuizType.GuessArabic -> screenState.arabicLeaderboard
                                                            QuizType.GuessBoth -> screenState.bothLeaderboard
                                                        }

                                                        val isLoading = when(type){
                                                            QuizType.GuessRoman -> screenState.romanLeaderboardLoading
                                                            QuizType.GuessArabic -> screenState.arabicLeaderboardLoading
                                                            QuizType.GuessBoth -> screenState.bothLeaderboardLoading
                                                        }

                                                        if(isLoading){
                                                            Box(
                                                                modifier = Modifier
                                                                    .fillMaxSize(),
                                                                contentAlignment = Alignment.Center
                                                            ){
                                                                CircularProgressIndicator(
                                                                    modifier = Modifier.align(Alignment.Center),
                                                                    color = MaterialTheme.colorScheme.onTertiary
                                                                )
                                                            }
                                                        }
                                                        else {
                                                            if(data.error != null || data.records == null) {
                                                                Column(
                                                                    modifier = Modifier.fillMaxSize(),
                                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                                    verticalArrangement = Arrangement.Center
                                                                ) {
                                                                    Text(
                                                                        text = data.error ?: stringResource(
                                                                            id = R.string.unknown_error
                                                                        ),
                                                                        color = MaterialTheme.colorScheme.onTertiary,
                                                                        style = MaterialTheme.typography.bodyLarge,
                                                                        textAlign = TextAlign.Center
                                                                    )
                                                                    Spacer(modifier = Modifier.height(24.dp))
                                                                    ActionButton(
                                                                        size = DpSize(120.dp, 46.dp),
                                                                        label = stringResource(
                                                                            id = R.string.refresh
                                                                        ),
                                                                        onClick = {
                                                                            viewModel.setUpLeaderboards(context as Activity)
                                                                        }
                                                                    )
                                                                }
                                                            }
                                                            else {
                                                                LeaderboardList(
                                                                    modifier = Modifier.fillMaxSize(),
                                                                    records = data.records,
                                                                    itemHeight = itemHeight
                                                                )
                                                            }

                                                        }


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
                                }
                            }
                            //records
                            else {
                                BoxWithConstraints(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    val width = maxWidth
                                    val height = maxHeight
                                    var showDeleteDialogBox by remember { mutableStateOf(false) }
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        //leaderboard title
                                        LeaderboardTitle(
                                            modifier = Modifier
                                                .padding(top = 12.dp),
                                            frameHeight = height * 0.25f,
                                            title = stringResource(
                                                id = R.string.records
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        //leaderboard list
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
                                                    /*item {
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                    }*/
                                                    itemsIndexed(
                                                        items = when (type) {
                                                            QuizType.GuessRoman -> screenState.romanRecords
                                                            QuizType.GuessArabic -> screenState.arabicRecords
                                                            QuizType.GuessBoth -> screenState.bothRecords
                                                        },
                                                        key = { _, item ->
                                                            item.id
                                                        }
                                                    ) { id, item ->
                                                        RecordsItemRow(
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
                                                                        message = context.getString(R.string.record_deleted),
                                                                        actionLabel = context.getString(R.string.quiz_restore),
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
                                                    QuizType.GuessRoman -> screenState.romanRecords.isEmpty()
                                                    QuizType.GuessArabic -> screenState.arabicRecords.isEmpty()
                                                    QuizType.GuessBoth -> screenState.bothRecords.isEmpty()
                                                }
                                                if(isListEmpty){
                                                    Box(modifier = Modifier.fillMaxSize()){
                                                        Text(
                                                            modifier = Modifier.align(Alignment.Center),
                                                            text = stringResource(id = R.string.quiz_empty_list),
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
                                                    message = context.getString(R.string.quiz_all_deleted),
                                                    actionLabel = context.getString(R.string.quiz_restore),
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        PageIndicator(
                            value = pagerState.currentPageOffsetFraction + pagerState.currentPage,
                            onPageClick = { pageID ->
                                scope.launch {
                                    pagerState.animateScrollToPage(pageID)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ActionButton(
                            label = stringResource(id = R.string.quiz_start),
                            onClick = {
                                viewModel.startQuiz(homeViewModel)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                //checking if leaderboard list is longer than the limit, if so - deleting last values
                LaunchedEffect(key1 = screenState.isQuizStarted, key2 = screenState.selectedType) {
                    if (!screenState.isQuizStarted) viewModel.limitRecordsList()
                }
            }
            //quiz screen
            else {
                LaunchedEffect(key1 = true) {
                    viewModel.loadInterstitialAd(context)
                    viewModel.loadRewardedAd(context)
                }
                var isPaused by rememberSaveable { mutableStateOf(false) }
                var isQuizOver by rememberSaveable { mutableStateOf(false) }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    //pause button
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
                        //quiz title
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                            contentAlignment = Alignment.Center
                        ){
                            val titleType = when(screenState.currentQuizType){
                                QuizType.GuessRoman -> stringResource(id = R.string.quiz_arabic)
                                QuizType.GuessArabic -> stringResource(id = R.string.quiz_roman)
                                QuizType.GuessBoth -> ""
                            }
                            AutoSizeText(
                                modifier = Modifier.padding(horizontal = 72.dp),
                                text =
                                    stringResource(id = R.string.quiz_start_title_1) + " " +
                                    titleType +
                                    stringResource(id = R.string.quiz_start_title_2),
                                maxLines = 2,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onTertiary,
                                maxTextSize = 22.sp,
                                minTextSize = MaterialTheme.typography.titleMedium.fontSize,
                                textAlign = TextAlign.Center
                            )
                        }

                        //quiz logic
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
                                    //ad logic
                                    if(!viewModel.isNewRecordSet()) {
                                        viewModel.showInterstitialAd(context)
                                    }
                                    viewModel.quizOver()
                                    isQuizOver = true
                                    isKeyboardActive = false
                                }
                            }
                        }
                        //check if guess successful
                        val mediaPlayer = remember { MediaPlayer.create(context, R.raw.sfx_quiz_correct) }
                        LaunchedEffect(key1 = screenState.currentInputValue) {
                            suspend fun onSuccess() {
                                showSuccessfulGuessIcon = true
                                isKeyboardActive = false
                                if(soundOn != 0) mediaPlayer.start()
                                viewModel.successfulGuess()
                                delay(1500)
                                viewModel.setValueToGuess(viewModel.nextValueToGuess())
                                showSuccessfulGuessIcon = false
                                isKeyboardActive = true
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
                            hideNumber = isPaused && !showSuccessfulGuessIcon,
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
                                homeViewModel.blurBackground(false)
                                viewModel.showInterstitialAd(context)
                                viewModel.stopQuiz(homeViewModel, context as Activity)
                            }
                        )
                        var showAdOption by remember { mutableStateOf(true) }
                        val egg by context.intSettings["egg"].collectAsStateWithLifecycle(initialValue = null)
                        QuizOverDialogBox(
                            show = isQuizOver,
                            onVisibilityChanged = {
                                homeViewModel.blurBackground(it)
                            },
                            onDismiss = {
                                viewModel.stopQuiz(homeViewModel, context as Activity)
                                homeViewModel.blurBackground(false)
                            },
                            onTryAgainClick = {
                                viewModel.setupTheQuiz()
                                startTimer = 3
                                isQuizOver = false
                                showAdOption = true
                            },
                            onWatchAdClick = if(showAdOption && viewModel.isRewardedAdLoaded() && egg != 1) {
                                {
                                    viewModel.showRewardedAd(
                                        context = context,
                                        onRewardReceived = {
                                            isQuizOver = false
                                            isKeyboardActive = true
                                            startTimer = 3
                                            viewModel.setGameTimer(20)
                                        }
                                    )
                                    showAdOption = false
                                }
                            } else null,
                            score = screenState.currentScore,
                            count = screenState.currentCount
                        )
                    }
                }
                //action when user presses back button
                BackHandler {
                    viewModel.showInterstitialAd(context)
                    viewModel.stopQuiz(homeViewModel, context as Activity)
                }
            }
        }
    }
}