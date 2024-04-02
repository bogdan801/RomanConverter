package com.bogdan801.romanconverter.presentation.components

import android.Manifest
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.presentation.theme.gray110
import com.bogdan801.romanconverter.presentation.util.OvalShape
import com.bogdan801.romanconverter.presentation.util.TextRecognitionAnalyzer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraTextRecognizer(
    modifier: Modifier,
    controller: LifecycleCameraController,
    cameraPermissionState: PermissionState,
    onTextRecognized: (String) -> Unit = {}
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = cameraPermissionState.status.isGranted) {
        if(!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ){
        val width = maxWidth
        Box(
            modifier = Modifier
                .width(width * (46f / 59f))
                .aspectRatio(23f / 28f)
                .clip(OvalShape)
                .background(gray110),
            contentAlignment = Alignment.Center
        ){
            if(cameraPermissionState.status.isGranted){
                val analyzer = remember {
                    TextRecognitionAnalyzer(
                        onDetectedTextUpdated = onTextRecognized
                    )
                }
                LaunchedEffect(key1 = true) {
                    controller.setImageAnalysisAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        analyzer
                    )
                }


                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    controller = controller
                )
            }
            else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            cameraPermissionState.launchPermissionRequest()
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Camera permission is not granted.",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Default,
                        textAlign = TextAlign.Center
                    )
                }

            }
        }

        Icon(
            modifier = Modifier
                .width(width)
                .aspectRatio(59f / 64f)
                .offset(x = (-1).dp, y = (-1).dp),
            painter = painterResource(id = R.drawable.ornament_camera_frame),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onTertiary
        )
    }
}