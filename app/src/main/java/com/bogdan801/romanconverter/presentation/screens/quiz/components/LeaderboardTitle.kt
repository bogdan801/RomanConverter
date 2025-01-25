package com.bogdan801.romanconverter.presentation.screens.quiz.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.presentation.components.AutoSizeText

@Composable
fun LeaderboardTitle(
    modifier: Modifier = Modifier,
    title: String = "LEADERBOARD",
    frameHeight: Dp
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ){
        Icon(
            modifier = Modifier
                .height(frameHeight)
                .width(frameHeight * 2.376f),
            painter = painterResource(
                id = R.drawable.ornament_leaderboard_titlebox
            ),
            contentDescription = "Leaderboard",
            tint = MaterialTheme.colorScheme.onTertiary
        )
        Box(
            modifier = Modifier
                .offset(y = frameHeight * 0.3f)
                .height(frameHeight * 0.55f)
                .width(frameHeight * 2.376f)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ){
            AutoSizeText(
                text = title,
                color = MaterialTheme.colorScheme.onTertiary,
                style = MaterialTheme.typography.titleLarge,
                minTextSize = MaterialTheme.typography.bodySmall.fontSize,
                maxTextSize = MaterialTheme.typography.titleLarge.fontSize
            )
        }
    }
}