package com.bogdan801.romanconverter.presentation.screens.quiz.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.presentation.components.AutoSizeText
import com.bogdan801.romanconverter.presentation.theme.green100
import com.bogdan801.romanconverter.presentation.theme.green200
import kotlinx.coroutines.delay

@Composable
fun QuizDisplay(
    modifier: Modifier = Modifier,
    startTimer: Int = 3,
    numberToGuess: String = "",
    inputValue: String = "",
    count: Int = 0,
    time: Int = 60,
    score: Int = 0,
    hideNumber: Boolean = false,
    showSuccessfulGuessIcon: Boolean = false
) {
    BoxWithConstraints(
        modifier = modifier.padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ){
        val frameWidth = min(maxWidth, 500.dp)
        val frameHeight = frameWidth / 1.93f
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                modifier = Modifier
                    .size(width = frameWidth, height = frameHeight)
                    .rotate(0.2f)
                    .offset(x = 0.62.dp, y = 2.dp)
                ,
                painter = painterResource(id = R.drawable.ornament_quiz_frame),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onTertiary
            )
            if(startTimer == 0){
                val counterRowWidth = frameWidth * 0.88f
                val counterRowHeight = frameHeight * 0.18f
                Box(modifier = Modifier.size(counterRowWidth, counterRowHeight)) {
                    ValueCounter(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(counterRowWidth / 3f)
                            .align(Alignment.Center),
                        value = count,
                        digitCount = 1,
                        rollUp = true
                    )
                    ValueCounter(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(counterRowWidth / 3f + 2.dp)
                            .align(Alignment.CenterStart),
                        value = score,
                        digitCount = 5,
                        rollUp = true
                    )
                    TimeCounter(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(counterRowWidth / 3f + 2.dp)
                            .align(Alignment.CenterEnd),
                        value = time
                    )
                }
                Box(modifier = Modifier.width(counterRowWidth)){
                    Text(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .width(counterRowWidth / 3f)
                            .align(Alignment.TopStart),
                        text = stringResource(id = R.string.quiz_score),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .width(counterRowWidth / 3f)
                            .align(Alignment.TopEnd),
                        text = stringResource(id = R.string.quiz_time),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiary,
                        textAlign = TextAlign.Center
                    )
                    Icon(
                        modifier = Modifier
                            .size(
                                width = frameWidth * 0.36f,
                                height = frameWidth * 0.0449f
                            )
                            .padding(top = 1.dp)
                            .align(Alignment.TopCenter),
                        painter = painterResource(id = R.drawable.ornament_quiz_bottom_decoration),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = frameHeight * 0.28f),
            visible = showSuccessfulGuessIcon,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Icon(
                modifier = Modifier
                    .size(64.dp)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        green100, green200, green100
                                    )
                                ),
                                blendMode = BlendMode.SrcAtop
                            )
                        }
                    },
                imageVector = Icons.Default.CheckCircleOutline,
                contentDescription = "Guessed successfully"
            )

        }

        if(!showSuccessfulGuessIcon){
            if(startTimer != 0) {
                AnimatedContent(
                    modifier = Modifier.align(Alignment.TopCenter),
                    targetState = startTimer,
                    transitionSpec = {
                        fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                    },
                    label = ""
                ) { value ->
                    Text(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = frameHeight / 3f + 8.dp),
                        text = value.toString(),
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }

            if(startTimer == 0) {
                AutoSizeText(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = frameHeight / 3f + 8.dp)
                        .padding(horizontal = 42.dp)
                        .then(
                            if (hideNumber) Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.onTertiary)
                            else Modifier
                        ),
                    text = numberToGuess,
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = MaterialTheme.typography.displayMedium,
                    maxTextSize = MaterialTheme.typography.displayMedium.fontSize
                )

                var showCursor by remember { mutableStateOf(true) }
                LaunchedEffect(key1 = startTimer) {
                    while(true){
                        delay(1000)
                        showCursor = !showCursor
                    }
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = frameHeight * 0.77f)
                        .padding(horizontal = 42.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    AutoSizeText(
                        text = inputValue,
                        style = MaterialTheme.typography.displaySmall.copy(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.primary,
                                )
                            )
                        ),
                        maxTextSize = MaterialTheme.typography.displaySmall.fontSize
                    )
                    Text(
                        modifier = Modifier.width(16.dp),
                        text = if(showCursor) "_" else "",
                        style = MaterialTheme.typography.displaySmall.copy(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.primary,
                                )
                            )
                        ),
                        //maxTextSize = MaterialTheme.typography.displaySmall.fontSize
                    )

                }
            }
        }
    }
}