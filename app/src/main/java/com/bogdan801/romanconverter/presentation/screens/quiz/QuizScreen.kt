package com.bogdan801.romanconverter.presentation.screens.quiz

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.presentation.components.ActionButton
import com.bogdan801.romanconverter.presentation.components.AutoSizeText
import com.bogdan801.romanconverter.presentation.components.PauseDialogBox
import com.bogdan801.romanconverter.presentation.components.QuizOverDialogBox
import com.bogdan801.romanconverter.presentation.components.QuizType
import com.bogdan801.romanconverter.presentation.components.QuizTypeSelector
import com.bogdan801.romanconverter.presentation.components.TimeCounter
import com.bogdan801.romanconverter.presentation.screens.home.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    navController: NavHostController,
    viewModel: QuizViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val homeScreenState by homeViewModel.screenState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
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
                aspectRatio = 11f/9f,
                type = QuizType.GuessRoman,
                isSelected = screenState.selectedType == QuizType.GuessRoman,
                onSelected = {
                    viewModel.setType(QuizType.GuessRoman)
                }
            )
            QuizTypeSelector(
                modifier = Modifier.weight(1f),
                aspectRatio = 11f/9f,
                type = QuizType.GuessArabic,
                isSelected = screenState.selectedType == QuizType.GuessArabic,
                onSelected = {
                    viewModel.setType(QuizType.GuessArabic)
                }
            )
        }
        QuizTypeSelector(
            modifier = Modifier
                .padding(horizontal = 24.dp),
            aspectRatio = 39f/9,
            type = QuizType.GuessBoth,
            isSelected = screenState.selectedType == QuizType.GuessBoth,
            onSelected = {
                viewModel.setType(QuizType.GuessBoth)
            }
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val height = maxHeight
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(height * 0.25f),
                    painter = painterResource(id = R.drawable.ornament_leaderboard_titlebox),
                    contentDescription = "Leaderboard",
                    tint = MaterialTheme.colorScheme.onTertiary
                )

            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        ActionButton(
            label = "START THE QUIZ",
            onClick = {
                Toast.makeText(context, "Quiz has started", Toast.LENGTH_SHORT).show()
            }
        )
        /*var value by remember { mutableIntStateOf(0) }
        var started by remember { mutableStateOf(false) }
        var show by remember { mutableStateOf(false) }
        var showLoseDialog by remember { mutableStateOf(false) }
        LaunchedEffect(key1 = started) {
            if(started){
                while(value > 0){
                    delay(1000)
                    value--
                }
                if(value == 0) {
                    started = false
                    showLoseDialog = true
                }
            }
        }

        ActionButton(
            label = "OPEN",
            onClick = {
                show = !show
            }
        )
        Spacer(modifier = Modifier.height(48.dp))

        Row {
            ActionButton(
                label = "+5",
                onClick = {
                    value += 5
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            ActionButton(
                label = "-5",
                onClick = {
                    if(value-5 >= 0) value -= 5
                }
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
        ActionButton(
            label = if(!started) "START" else "STOP",
            onClick = {
                started = !started
            }
        )
        Spacer(modifier = Modifier.height(48.dp))
        TimeCounter(
            modifier = Modifier.size(100.dp, 30.dp),
            value = value
        )
        Spacer(modifier = Modifier.height(48.dp))
        ActionButton(
            label = if(homeScreenState.isNavBarExpanded) "HIDE" else "SHOW",
            onClick = {
                homeViewModel.showNavBar(!homeScreenState.isNavBarExpanded)
            }
        )

        PauseDialogBox(
            modifier = Modifier,
            show = show,
            onVisibilityChanged = { isVisible ->
                homeViewModel.blurBackground(isVisible)
            },
            onDismiss = {
                show = false
            },
            onHomeClick = {}
        )
        QuizOverDialogBox(
            modifier = Modifier,
            show = showLoseDialog,
            onVisibilityChanged = { isVisible ->
                homeViewModel.blurBackground(isVisible)
            },
            onDismiss = {
                showLoseDialog = false
            },
            score = 1639,
            count = 23,
            onHomeClick = {}
        )*/
    }
}