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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import com.bogdan801.romanconverter.presentation.theme.leaderboardItemGradientBrush
import java.time.LocalDate

@Composable
fun LeaderboardItemRow(
    modifier: Modifier = Modifier,
    position: Int = 1,
    data: LeaderboardItem = LeaderboardItem(),
    onDeleteClick: () -> Unit = {},
    onDeleteAllClick: () -> Unit = {},
    showDropDownMenu: Boolean = true
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
                            if(showDropDownMenu){
                                showDropDown = true
                                longClickOffset = DpOffset(x = offset.x.toDp(), y = offset.y.toDp())
                            }
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
                text = "$position. ${convertToLocalDateString(date = data.date)}",
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

@Composable
fun convertToLocalDateString(date: LocalDate): String {
    val monthName = when(date.monthValue){
        1 -> stringResource(id = R.string.month_jan)
        2 -> stringResource(id = R.string.month_feb)
        3 -> stringResource(id = R.string.month_mar)
        4 -> stringResource(id = R.string.month_apr)
        5 -> stringResource(id = R.string.month_may)
        6 -> stringResource(id = R.string.month_jun)
        7 -> stringResource(id = R.string.month_jul)
        8 -> stringResource(id = R.string.month_aug)
        9 -> stringResource(id = R.string.month_sep)
        10 -> stringResource(id = R.string.month_oct)
        11 -> stringResource(id = R.string.month_nov)
        12 -> stringResource(id = R.string.month_dec)
        else -> ""
    }
    return "${date.dayOfMonth} $monthName ${date.year}"
}