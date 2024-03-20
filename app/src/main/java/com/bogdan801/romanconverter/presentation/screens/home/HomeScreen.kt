package com.bogdan801.romanconverter.presentation.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.drawToBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bogdan801.romanconverter.presentation.components.InputButton
import com.bogdan801.romanconverter.presentation.components.SmallIconButton
import com.bogdan801.util_library.intSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sqrt

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val view = LocalView.current
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent
    ) { systemPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            /*Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                var previousRoute by remember { mutableStateOf("") }
                Navigation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    navController = navController,
                    previousRoute = previousRoute,
                    convertScreen = {
                        ConvertScreen(
                            navController = navController,
                            homeViewModel = viewModel
                        )
                    },
                    quizScreen = {
                        QuizScreen(
                            navController = navController,
                            homeViewModel = viewModel
                        )
                    },
                    cameraScreen = {
                        CameraScreen(
                            navController = navController,
                            homeViewModel = viewModel
                        )
                    }
                )

                Row(
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            previousRoute = navController.currentDestination?.route ?: ""
                            if(navController.currentDestination?.route!=Screen.Convert.route){
                                navController.navigate(Screen.Convert.route){
                                    popUpTo(0)
                                }
                            }
                        }
                    ) {
                        Text(text = "1")
                    }
                    Button(
                        onClick = {
                            previousRoute = navController.currentDestination?.route ?: ""
                            if(navController.currentDestination?.route!=Screen.Quiz.route){
                                navController.navigate(Screen.Quiz.route){
                                    popUpTo(0)
                                }
                            }
                        }
                    ) {
                        Text(text = "2")
                    }
                    Button(
                        onClick = {
                            previousRoute = navController.currentDestination?.route ?: ""
                            if(navController.currentDestination?.route!=Screen.Camera.route){
                                navController.navigate(Screen.Camera.route){
                                    popUpTo(0)
                                }
                            }
                        }
                    ) {
                        Text(text = "3")
                    }
                }
            }*/


            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InputButton(label = "i")
                        InputButton(label = "x")
                        InputButton(label = "c")
                        InputButton(label = "m")
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InputButton(label = "v")
                        InputButton(label = "l")
                        InputButton(label = "d")
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InputButton(label = "9")
                        InputButton(label = "I")
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InputButton(isBackspace = true)
                    }
                }
            }


            //dark theme switch
            val currentTheme = context.intSettings["theme"].collectAsState(initial = 2)
            var iconCoordinates by remember { mutableStateOf(Offset(300f, 300f)) }
            var imageState by remember { mutableStateOf<ImageBitmap?>(null) }
            var isAnimating by remember { mutableStateOf(false)}
            val animatable = remember { Animatable(0f) }
            var buttonEnabled by remember { mutableStateOf(true) }
            SmallIconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 12.dp, top = 12.dp + systemPadding.calculateTopPadding())
                    .onGloballyPositioned { coordinates ->
                        scope.launch {
                            iconCoordinates = coordinates.boundsInRoot().center
                        }
                    },
                onClick = {
                    if (buttonEnabled) {
                        buttonEnabled = false

                        scope.launch {
                            imageState = view.drawToBitmap().asImageBitmap()
                            context.intSettings.set("theme", ((currentTheme.value ?: 0) + 1) % 2)

                            isAnimating = true
                            val diagonal = sqrt((view.width * view.width + view.height * view.height).toFloat())
                            animatable.animateTo(
                                diagonal,
                                tween(durationMillis = 500, easing = EaseInOut)
                            )
                            isAnimating = false
                            animatable.snapTo(0f)
                        }

                        scope.launch {
                            delay(400)
                            buttonEnabled = true
                        }
                    }
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
                    imageVector = when(currentTheme.value){
                        0 -> Icons.Outlined.DarkMode
                        1 -> Icons.Outlined.LightMode
                        else -> Icons.Outlined.DarkMode
                    },
                    contentDescription = "Dark Mode Switch",
                )
            }

            //animation while switching a theme
            if (isAnimating){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = 0.99f)
                    .drawBehind {
                        if (imageState != null) {
                            drawImage(imageState!!)
                            drawCircle(
                                color = Color.Black,
                                radius = animatable.value,
                                center = iconCoordinates,
                                blendMode = BlendMode.Xor
                            )
                        }
                    }
                )
            }
        }
    }
}