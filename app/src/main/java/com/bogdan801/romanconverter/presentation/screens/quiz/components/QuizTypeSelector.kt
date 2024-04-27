package com.bogdan801.romanconverter.presentation.screens.quiz.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.domain.model.QuizType

@Composable
fun QuizTypeSelector(
    modifier: Modifier = Modifier,
    selectedType: QuizType = QuizType.GuessRoman,
    onTypeSelected: (QuizType) -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val width = maxWidth
        val height = maxHeight
        val ratio = width / height
        if(ratio > 2){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuizTypeSelectorCard(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    type = QuizType.GuessRoman,
                    isSelected = selectedType == QuizType.GuessRoman,
                    onSelected = {
                        onTypeSelected(QuizType.GuessRoman)
                    },
                    isSmall = true
                )
                QuizTypeSelectorCard(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    type = QuizType.GuessArabic,
                    isSelected = selectedType == QuizType.GuessArabic,
                    onSelected = {
                        onTypeSelected(QuizType.GuessArabic)
                    },
                    isSmall = true
                )
                QuizTypeSelectorCard(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    type = QuizType.GuessBoth,
                    isSelected = selectedType == QuizType.GuessBoth,
                    onSelected = {
                        onTypeSelected(QuizType.GuessBoth)
                    },
                    isSmall = true
                )
            }
        }
        else {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.7f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    QuizTypeSelectorCard(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        type = QuizType.GuessRoman,
                        isSelected = selectedType == QuizType.GuessRoman,
                        onSelected = {
                            onTypeSelected(QuizType.GuessRoman)
                        }
                    )
                    QuizTypeSelectorCard(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        type = QuizType.GuessArabic,
                        isSelected = selectedType == QuizType.GuessArabic,
                        onSelected = {
                            onTypeSelected(QuizType.GuessArabic)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                QuizTypeSelectorCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    type = QuizType.GuessBoth,
                    isSelected = selectedType == QuizType.GuessBoth,
                    onSelected = {
                        onTypeSelected(QuizType.GuessBoth)
                    }
                )
            }
        }
    }
}