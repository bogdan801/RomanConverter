package com.bogdan801.romanconverter.presentation.screens.camera

import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.romanconverter.presentation.components.CameraPreview
import com.bogdan801.romanconverter.presentation.screens.home.HomeViewModel
import com.bogdan801.romanconverter.presentation.util.OvalShape
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    navController: NavHostController,
    viewModel: CameraViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    LaunchedEffect(key1 = cameraPermissionState.status.isGranted) {
        if(!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(64.dp)
                .aspectRatio(23f / 28f)
                .clip(OvalShape)
                .background(MaterialTheme.colorScheme.onTertiary),
            contentAlignment = Alignment.Center
        ){
            if(cameraPermissionState.status.isGranted){
                val lifecycleOwner = LocalLifecycleOwner.current
                val controller = remember {
                    LifecycleCameraController(context).apply {
                        setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
                    }
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
                        text = "Camera permission is not granted.\n Click to grant permission",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Default,
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }
}