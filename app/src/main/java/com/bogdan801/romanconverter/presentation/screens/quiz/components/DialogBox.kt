package com.bogdan801.romanconverter.presentation.screens.quiz.components

import android.media.MediaPlayer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.presentation.components.ActionButton
import com.bogdan801.romanconverter.presentation.theme.dialogBoxGradientBrush
import com.bogdan801.romanconverter.presentation.theme.green100
import com.bogdan801.util_library.intSettings

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
    modifier: Modifier = Modifier,
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
            text = stringResource(id = R.string.dialog_paused),
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
                label = stringResource(id = R.string.dialog_continue),
                maxTextSize = 17.sp,
                textStyle = MaterialTheme.typography.bodyMedium,
                shadowColor = Color.Black.copy(alpha = 0.07f),
                onClick = onContinueClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            ActionButton(
                size = DpSize(110.dp, 38.dp),
                label = stringResource(id = R.string.dialog_home),
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
    modifier: Modifier = Modifier,
    show: Boolean = false,
    onVisibilityChanged: (Boolean) -> Unit = {},
    onDismiss: () -> Unit = {},
    score: Int = 0,
    count: Int = 0,
    onTryAgainClick: () -> Unit = {},
    onHomeClick: () -> Unit = { onDismiss() },
    onWatchAdClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val soundOn by context.intSettings["sound_on"].collectAsStateWithLifecycle(initialValue = 1)
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.sfx_quiz_over) }
    LaunchedEffect(key1 = show) {
        onVisibilityChanged(show)
        if(show && soundOn != 0) mediaPlayer.start()
    }
    BaseDialogBox(
        modifier = modifier.size(300.dp, if(onWatchAdClick == null) 322.dp else 376.dp),
        show = show,
        onDismiss = onDismiss,
        usePlatformDefaultWidth = true
    ){
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.dialog_over),
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
                    text = stringResource(id = R.string.dialog_score),
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
                    text = stringResource(id = R.string.dialog_count),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.Center
                )
            }
        }
        Row {
            ActionButton(
                size = DpSize(110.dp, 38.dp),
                label = stringResource(id = R.string.dialog_try_again),
                maxTextSize = 17.sp,
                insidePadding = 12.dp,
                textStyle = MaterialTheme.typography.bodyMedium,
                shadowColor = Color.Black.copy(alpha = 0.07f),
                onClick = onTryAgainClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            ActionButton(
                size = DpSize(110.dp, 38.dp),
                label = stringResource(id = R.string.dialog_home),
                maxTextSize = 15.sp,
                insidePadding = 12.dp,
                textStyle = MaterialTheme.typography.bodyMedium,
                borderColor = MaterialTheme.colorScheme.outlineVariant,
                shadowColor = Color.Black.copy(alpha = 0.07f),
                onClick = onHomeClick
            )
        }
        if(onWatchAdClick != null){
            Spacer(modifier = Modifier.height(16.dp))
            ActionButton(
                size = DpSize(236.dp, 38.dp),
                label = stringResource(id = R.string.dialog_watch_ad),
                maxTextSize = 14.sp,
                borderColor = green100,
                textStyle = MaterialTheme.typography.bodyMedium,
                shadowColor = Color.Black.copy(alpha = 0.07f),
                onClick = onWatchAdClick
            )
        }
    }
}

@Composable
fun DeleteConfirmDialogBox(
    modifier: Modifier = Modifier,
    show: Boolean = false,
    onVisibilityChanged: (Boolean) -> Unit = {},
    onCancelClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = show) {
        onVisibilityChanged(show)
    }
    BaseDialogBox(
        modifier = modifier.size(300.dp, 232.dp),
        show = show,
        onDismiss = onCancelClick,
        usePlatformDefaultWidth = true
    ){
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.dialog_delete_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onTertiary
        )
        Spacer(modifier = Modifier.height(15.dp))
        Icon(
            painter = painterResource(id = R.drawable.ornament_dialog_box),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onTertiary
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(1f),
            contentAlignment = Alignment.Center
        ){
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.dialog_delete_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary,
                textAlign = TextAlign.Center
            )
        }
        Row(modifier = Modifier.padding(bottom = 24.dp)) {
            ActionButton(
                size = DpSize(110.dp, 38.dp),
                label = stringResource(id = R.string.dialog_cancel),
                textStyle = MaterialTheme.typography.bodyMedium,
                borderColor = MaterialTheme.colorScheme.outlineVariant,
                shadowColor = Color.Black.copy(alpha = 0.07f),
                onClick = onCancelClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            ActionButton(
                size = DpSize(110.dp, 38.dp),
                label = stringResource(id = R.string.dialog_confirm),
                textStyle = MaterialTheme.typography.bodyMedium,
                shadowColor = Color.Black.copy(alpha = 0.07f),
                onClick = onConfirmClick
            )
        }
    }
}