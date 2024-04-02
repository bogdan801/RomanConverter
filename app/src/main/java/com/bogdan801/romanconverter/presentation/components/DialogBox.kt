package com.bogdan801.romanconverter.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.presentation.theme.dialogBoxGradientBrush

@Composable
fun BaseDialogBox(
    modifier: Modifier = Modifier,
    show: Boolean = false,
    onDismiss: () -> Unit = {},
    scrimOpacity: Float = 0.35f,
    usePlatformDefaultWidth: Boolean = false,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    if(show){
        Dialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = usePlatformDefaultWidth
            ),
            onDismissRequest = onDismiss
        ) {
            (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(scrimOpacity)
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(dialogBoxGradientBrush())
            ){
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onTertiary),
                    color = Color.Transparent
                ){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        content(this)
                    }
                }
            }
        }
    }
}

@Composable
fun PauseDialogBox(
    modifier: Modifier,
    show: Boolean = false,
    onVisibilityChanged: (Boolean) -> Unit = {},
    onDismiss: () -> Unit = {},
    onContinueClick: () -> Unit = { onDismiss() },
    onHomeClick: () -> Unit = {}
) {
    LaunchedEffect(key1 = show) {
        onVisibilityChanged(show)
    }
    BaseDialogBox(
        modifier = modifier.size(300.dp, 182.dp),
        show = show,
        onDismiss = onDismiss,
        usePlatformDefaultWidth = true
    ){
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "PAUSED",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onTertiary
        )
        Spacer(modifier = Modifier.height(15.dp))
        Icon(
            painter = painterResource(id = R.drawable.ornament_dialog_box),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onTertiary
        )
        Spacer(modifier = Modifier.height(17.dp))
        Row {
            ActionButton(
                size = DpSize(110.dp, 38.dp),
                label = "CONTINUE",
                maxTextSize = 17.sp,
                textStyle = MaterialTheme.typography.bodyMedium,
                shadowColor = Color.Black.copy(alpha = 0.07f),
                onClick = onContinueClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            ActionButton(
                size = DpSize(110.dp, 38.dp),
                label = "HOME",
                maxTextSize = 15.sp,
                textStyle = MaterialTheme.typography.bodyMedium,
                borderColor = MaterialTheme.colorScheme.outlineVariant,
                shadowColor = Color.Black.copy(alpha = 0.07f),
                onClick = onHomeClick
            )
        }
    }
}

@Composable
fun QuizOverDialogBox(
    modifier: Modifier,
    show: Boolean = false,
    onVisibilityChanged: (Boolean) -> Unit = {},
    onDismiss: () -> Unit = {},
    score: Int = 0,
    count: Int = 0,
    onTryAgainClick: () -> Unit = { onDismiss() },
    onHomeClick: () -> Unit = {}
) {
    LaunchedEffect(key1 = show) {
        onVisibilityChanged(show)
    }
    BaseDialogBox(
        modifier = modifier.size(300.dp, 322.dp),
        show = show,
        onDismiss = onDismiss,
        usePlatformDefaultWidth = true
    ){
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "QUIZ IS OVER",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onTertiary
        )
        Spacer(modifier = Modifier.height(15.dp))
        Icon(
            painter = painterResource(id = R.drawable.ornament_dialog_box),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onTertiary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(154.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier
                        .rotate(180f),
                    painter = painterResource(id = R.drawable.ornament_counter),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onTertiary
                )
                Spacer(modifier = Modifier.height(1.dp))
                ValueCounter(
                    modifier = Modifier.size(95.dp, 30.dp),
                    value = score
                )
                Spacer(modifier = Modifier.height(1.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ornament_counter),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onTertiary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "THE\nSCORE",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier
                        .rotate(180f),
                    painter = painterResource(id = R.drawable.ornament_counter),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onTertiary
                )
                Spacer(modifier = Modifier.height(1.dp))
                ValueCounter(
                    modifier = Modifier.size(95.dp, 30.dp),
                    value = count,
                    digitCount = 1,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.height(1.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ornament_counter),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onTertiary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "NUMBERS\nGUESSED",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.Center
                )
            }
        }
        Row {
            ActionButton(
                size = DpSize(110.dp, 38.dp),
                label = "TRY AGAIN",
                maxTextSize = 17.sp,
                textStyle = MaterialTheme.typography.bodyMedium,
                shadowColor = Color.Black.copy(alpha = 0.07f),
                onClick = onTryAgainClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            ActionButton(
                size = DpSize(110.dp, 38.dp),
                label = "HOME",
                maxTextSize = 15.sp,
                textStyle = MaterialTheme.typography.bodyMedium,
                borderColor = MaterialTheme.colorScheme.outlineVariant,
                shadowColor = Color.Black.copy(alpha = 0.07f),
                onClick = onHomeClick
            )
        }
    }
}