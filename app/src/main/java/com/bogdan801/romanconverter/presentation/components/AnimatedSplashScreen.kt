package com.bogdan801.romanconverter.presentation.components

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.R
import kotlinx.coroutines.delay

@Composable
fun AnimatedSplashScreen(
    modifier: Modifier = Modifier,
    duration: Long = 2500
) {
    var show by rememberSaveable { mutableStateOf(Build.VERSION.SDK_INT < Build.VERSION_CODES.S) }
    LaunchedEffect(key1 = true) {
        delay(duration)
        show = false
    }

    AnimatedVisibility(
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(156.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ){
                var iOffset by remember { mutableStateOf(120.dp) }
                var xOffset by remember { mutableStateOf(120.dp) }
                var nineOffset by remember { mutableStateOf(120.dp) }

                val iOffsetAnimation by animateDpAsState(
                    targetValue = iOffset,
                    label = ""
                )

                val xOffsetAnimation by animateDpAsState(
                    targetValue = xOffset,
                    label = ""
                )

                val nineOffsetAnimation by animateDpAsState(
                    targetValue = nineOffset,
                    label = ""
                )

                val stayDuration: Long = 1300

                LaunchedEffect(true) {
                    delay(250)
                    iOffset = 0.dp
                    delay(stayDuration)
                    iOffset = (-120).dp
                }

                LaunchedEffect(true) {
                    delay(600)
                    xOffset = 0.dp
                    delay(stayDuration - 200)
                    xOffset = (-120).dp
                }

                LaunchedEffect(true) {
                    delay(stayDuration + 500)
                    nineOffset = 0.dp
                }


                Image(
                    painter = painterResource(id = R.drawable.ic_splash_background),
                    contentDescription = "",
                    contentScale = ContentScale.Fit
                )
                Row {
                    Image(
                        modifier = Modifier.offset {
                            IntOffset(0, iOffsetAnimation.roundToPx())
                        },
                        painter = painterResource(id = R.drawable.ic_spash_i),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        modifier = Modifier.offset {
                            IntOffset(0, xOffsetAnimation.roundToPx())
                        },
                        painter = painterResource(id = R.drawable.ic_splash_x),
                        contentDescription = "",
                        contentScale = ContentScale.Fit
                    )
                }
                Image(
                    modifier = Modifier.offset {
                        IntOffset(0, nineOffsetAnimation.roundToPx())
                    },
                    painter = painterResource(id = R.drawable.ic_splas_9),
                    contentDescription = "",
                    contentScale = ContentScale.Fit
                )
            }
        }

    }
}