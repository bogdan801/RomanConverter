package com.bogdan801.romanconverter.presentation.screens.quiz

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import com.bogdan801.romanconverter.domain.model.QuizType
import com.bogdan801.romanconverter.presentation.components.ActionButton
import com.bogdan801.romanconverter.presentation.components.AutoSizeText
import com.bogdan801.romanconverter.presentation.components.DeleteConfirmDialogBox
import com.bogdan801.romanconverter.presentation.components.LeaderboardItemRow
import com.bogdan801.romanconverter.presentation.components.QuizTypeSelector
import com.bogdan801.romanconverter.presentation.screens.home.HomeViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.random.Random

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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
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
                        maxTextSize = 24.sp,
                        minTextSize = MaterialTheme.typography.titleMedium.fontSize,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        QuizTypeSelector(
                            modifier = Modifier.weight(1f),
                            aspectRatio = 11f / 9f,
                            type = QuizType.GuessRoman,
                            isSelected = screenState.selectedType == QuizType.GuessRoman,
                            onSelected = {
                                viewModel.setType(QuizType.GuessRoman)
                                snackbarHostState.currentSnackbarData?.dismiss()
                            }
                        )
                        QuizTypeSelector(
                            modifier = Modifier.weight(1f),
                            aspectRatio = 11f / 9f,
                            type = QuizType.GuessArabic,
                            isSelected = screenState.selectedType == QuizType.GuessArabic,
                            onSelected = {
                                viewModel.setType(QuizType.GuessArabic)
                                snackbarHostState.currentSnackbarData?.dismiss()
                            }
                        )
                    }
                    QuizTypeSelector(
                        modifier = Modifier
                            .padding(horizontal = 24.dp),
                        aspectRatio = 39f / 9,
                        type = QuizType.GuessBoth,
                        isSelected = screenState.selectedType == QuizType.GuessBoth,
                        onSelected = {
                            viewModel.setType(QuizType.GuessBoth)
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
                                modifier = Modifier
                                    .fillMaxSize()

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
                            //viewModel.startQuiz(true, homeViewModel)

                            val score = Random.nextInt(10000, 25000)
                            viewModel.saveRecord(
                                LeaderboardItem(
                                    id = Random.nextInt(0, 100000),
                                    date = LocalDate.now().minusDays(Random.nextLong(0, 365)),
                                    score = score,
                                    count = score / 800
                                )
                            )
                            /*scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(message = "Quiz has started", duration = SnackbarDuration.Short)
                            }*/
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            else {
                BackHandler {
                    viewModel.startQuiz(false, homeViewModel)
                }
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ){
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Типу екран гри\nУяви типу це вже зроблено\nгииииииииииии", color = Color.White, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(32.dp))
                        ActionButton(label = "GO BACK") {
                            viewModel.startQuiz(false, homeViewModel)
                        }
                    }
                }
            }
        }
    }
}