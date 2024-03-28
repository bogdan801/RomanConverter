package com.bogdan801.romanconverter.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.bogdan801.romanconverter.presentation.theme.dialogBoxGradientBrush

@Composable
fun BaseDialogBox(
    modifier: Modifier = Modifier,
    show: Boolean = false,
    onDismiss: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {}
) {
    if(show){
        Dialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            onDismissRequest = onDismiss
        ) {
            (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0.0f)
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(dialogBoxGradientBrush()),
                contentAlignment = Alignment.Center
            ){
                content(this)
            }
        }
    }
}

@Composable
fun PauseDialogBox(
    modifier: Modifier
) {

}