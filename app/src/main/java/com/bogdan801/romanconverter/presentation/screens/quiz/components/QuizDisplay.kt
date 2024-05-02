package com.bogdan801.romanconverter.presentation.screens.quiz.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.presentation.theme.green100
import com.bogdan801.romanconverter.presentation.theme.green200
import com.bogdan801.romanconverter.presentation.util.secondsToTimeString

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
        modifier = modifier
            .padding(horizontal = 16.dp)
            .height(300.dp),
        contentAlignment = Alignment.Center
    ){
        val width = maxWidth
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if(startTimer != 0) {
                AnimatedContent(targetState = startTimer, label = "") { value ->
                    Text(
                        text = value.toString(),
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }
            else if(showSuccessfulGuessIcon){
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
            else {
                Text(
                    modifier = Modifier.then(
                        if(hideNumber) Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.onTertiary)
                        else Modifier
                    ),
                    text = numberToGuess,
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = inputValue,
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    Text(
                        text = score.toString(),
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = count.toString(),
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = time.secondsToTimeString(),
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }
        }
    }
}