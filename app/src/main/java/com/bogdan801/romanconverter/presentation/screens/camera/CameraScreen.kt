package com.bogdan801.romanconverter.presentation.screens.camera

import android.Manifest
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FlashlightOff
import androidx.compose.material.icons.outlined.FlashlightOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bogdan801.romanconverter.presentation.components.AutoSizeText
import com.bogdan801.romanconverter.presentation.components.CameraTextRecognizer
import com.bogdan801.romanconverter.presentation.components.RecognizedTextDisplay
import com.bogdan801.romanconverter.presentation.components.SmallIconButton
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(cameraPermissionState.status.isGranted){
            SmallIconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                onClick = {
                    val newState = !screenState.isFlashlightOn
                    controller.enableTorch(newState)
                    viewModel.setFlashlightState(newState)
                }
            ){
                val primary = MaterialTheme.colorScheme.primary
                val secondary = MaterialTheme.colorScheme.secondary
                Icon(
                    modifier = Modifier
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            primary, secondary, primary
                                        )
                                    ),
                                    blendMode = BlendMode.SrcAtop
                                )
                            }
                        },
                    imageVector = if(!screenState.isFlashlightOn) Icons.Outlined.FlashlightOn
                    else Icons.Outlined.FlashlightOff,
                    contentDescription = "Flashlight Switch",
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 72.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AutoSizeText(
                modifier = Modifier.padding(horizontal = 84.dp),
                text = "Point the camera at the\nRoman numerals",
                maxLines = 2,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiary,
                maxTextSize = 26.sp,
                minTextSize = MaterialTheme.typography.titleMedium.fontSize,
                textAlign = TextAlign.Center
            )
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.TopCenter
            ){
                val width = maxWidth
                val height = maxHeight
                val previewWidth = if(width < height) width - 64.dp else height * (59f/64f)
                CameraTextRecognizer(
                    modifier = Modifier
                        .width(previewWidth),
                    controller = controller,
                    cameraPermissionState = cameraPermissionState,
                    onTextRecognized = { analyzedText ->
                        viewModel.setNewAnalyzedValue(analyzedText)
                    }
                )
            }
            RecognizedTextDisplay(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                recognizedText = screenState.recognizedText
            )
        }
    }
}