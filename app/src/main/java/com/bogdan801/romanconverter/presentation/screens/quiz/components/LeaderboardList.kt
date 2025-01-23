package com.bogdan801.romanconverter.presentation.screens.quiz.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.domain.model.LeaderboardData
import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import com.bogdan801.romanconverter.domain.model.QuizType
import com.bogdan801.romanconverter.presentation.components.ActionButton

@Composable
fun LeaderboardList(
    modifier: Modifier = Modifier,
    records: List<LeaderboardItem>? = null,
    itemHeight: Dp = 48.dp,
    maxItemCount: Int = 20
) {
    val displayList by remember {
        derivedStateOf {
            records?.let { records ->
                if (records.size > maxItemCount) {
                    val sublist = records.subList(0, maxItemCount)
                    val user = records.find { it.isUser }
                    when {
                        user == null -> sublist
                        sublist.any { it.isUser } -> sublist
                        else -> sublist + listOf(null, user)
                    }
                }
                else records
            }
        }
    }

    Box(modifier.fillMaxSize(), Alignment.Center){
        if(displayList != null){
            if (displayList!!.isNotEmpty()){
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    items(displayList!!){ item ->
                        LeaderboardItemRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(itemHeight),
                            data = item
                        )
                    }
                }
            }
            else {
                Text(
                    text = "Leaderboard is empty, be the first one\n to set the record",
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }


}