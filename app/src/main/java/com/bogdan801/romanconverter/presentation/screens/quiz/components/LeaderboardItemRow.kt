package com.bogdan801.romanconverter.presentation.screens.quiz.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import com.bogdan801.romanconverter.presentation.theme.leaderboardItemGradientBrush
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun LeaderboardItemRow(
    modifier: Modifier = Modifier,
    position: Int = 1,
    data: LeaderboardItem = LeaderboardItem(),
    onDeleteClick: () -> Unit = {},
    onDeleteAllClick: () -> Unit = {}
) {
    var showDropDown by remember { mutableStateOf(false) }
    var longClickOffset by remember { mutableStateOf(DpOffset.Zero) }
    val interactionSource = remember { MutableInteractionSource() }

    BoxWithConstraints(
        modifier = modifier
    ) {
        val height = maxHeight
        Row(
            modifier = Modifier
                .fillMaxSize()
                .indication(interactionSource, LocalIndication.current)
                .pointerInput(true) {
                    detectTapGestures(
                        onLongPress = { offset ->
                            showDropDown = true
                            longClickOffset = DpOffset(x = offset.x.toDp(), y = offset.y.toDp())
                        },
                        onPress = {
                            val press = PressInteraction.Press(it)
                            interactionSource.emit(press)
                            tryAwaitRelease()
                            interactionSource.emit(PressInteraction.Release(press))
                        }
                    )
                }
                .background(brush = leaderboardItemGradientBrush())
                .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(0.6f),
                text = "$position. ${data.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.US))}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Text(
                modifier = Modifier.weight(0.2f),
                text = "${data.count}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary,
                textAlign = TextAlign.End
            )
            Text(
                modifier = Modifier.weight(0.2f),
                text = "${data.score}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary,
                textAlign = TextAlign.End
            )
        }


        DropdownMenu(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            ),
            expanded = showDropDown,
            onDismissRequest = { showDropDown = false },
            offset = longClickOffset.copy(y = longClickOffset.y - height)
        ) {
            DropdownMenuItem(
                text = {
                    Text("Delete record")
                },
                colors = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.onTertiary
                ),
                onClick = {
                    onDeleteClick()
                    showDropDown = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text("Delete all records")
                },
                colors = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.onTertiary
                ),
                onClick = {
                    onDeleteAllClick()
                    showDropDown = false
                }
            )
        }
    }
}

