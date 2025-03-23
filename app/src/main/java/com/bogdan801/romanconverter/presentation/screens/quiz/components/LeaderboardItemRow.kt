package com.bogdan801.romanconverter.presentation.screens.quiz.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import com.bogdan801.romanconverter.presentation.theme.bronze
import com.bogdan801.romanconverter.presentation.theme.leaderboardItemGradientBrush
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LeaderboardItemRow(
    modifier: Modifier = Modifier,
    data: LeaderboardItem?
) {
    val context = LocalContext.current
    var allowClick by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    Row(
        modifier = modifier
            .background(brush = leaderboardItemGradientBrush())
            .border(
                width = 0.5.dp,
                color = if (data != null && data.isUser) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f)
            )
            .then(
                if (data?.isPrivate == true && allowClick)
                    Modifier.clickable {
                        Toast
                            .makeText(
                                context,
                                context.getString(R.string.quiz_private_score_message),
                                Toast.LENGTH_LONG
                            )
                            .show()
                        scope.launch {
                            allowClick = false
                            delay(3500)
                            allowClick = true
                        }
                    }
                else Modifier
            )
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (data != null) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text = "${data.rank}.  ${data.username}"
                Text(
                    modifier = Modifier.widthIn(max = 260.dp),
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                when (data.rank) {
                    1 -> {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_gold),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    2 -> {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_silver),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                    3 -> {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bronze),
                            contentDescription = "",
                            tint = bronze
                        )
                    }
                }
                if (data.isPrivate){
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        modifier = Modifier.widthIn(max = 260.dp),
                        text = stringResource(id = R.string.quiz_score_private),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primaryContainer,
                    )
                    Icon(
                        modifier = Modifier.size(14.dp),
                        imageVector = Icons.Default.Info,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${data.score}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
        } else {
            val color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.8f)
            Row(
                modifier = Modifier.padding(start = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}