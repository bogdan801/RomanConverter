package com.bogdan801.romanconverter.presentation.screens.home

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.drawToBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.presentation.screens.home.components.NavigationBar
import com.bogdan801.romanconverter.presentation.screens.home.components.NavigationItem
import com.bogdan801.romanconverter.presentation.components.SmallIconButton
import com.bogdan801.romanconverter.presentation.navigation.Navigation
import com.bogdan801.romanconverter.presentation.navigation.Screen
import com.bogdan801.romanconverter.presentation.screens.camera.CameraScreen
import com.bogdan801.romanconverter.presentation.screens.convert.ConvertScreen
import com.bogdan801.romanconverter.presentation.screens.quiz.QuizScreen
import com.bogdan801.util_library.intSettings
import com.skydoves.cloudy.Cloudy
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sqrt

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: HomeViewModel = hiltViewModel(),
    themeID: Int? = null
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val view = LocalView.current
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent
    ) { systemPadding ->
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            //background texture
            val widthInPx = with(LocalDensity.current) { maxWidth.toPx() }
            val scale = 1.1f
            val offset = ((scale - 1f) / 2f) * widthInPx
            val offsetX by animateFloatAsState(
                targetValue = when(navController.currentDestination?.route){
                    Screen.Convert.route -> offset
                    Screen.Quiz.route -> 0f
                    Screen.Camera.route -> -offset
                    else -> 0f
                },
                label = ""
            )
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX
                    ),
                painter = painterResource(
                    id = when(themeID){
                        0 -> R.drawable.white_texture
                        1 -> R.drawable.black_texture
                        else -> {
                            if (!isSystemInDarkTheme()) R.drawable.white_texture
                            else R.drawable.black_texture
                        }
                    }
                ),
                contentDescription = "background",
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                var showNavBar by remember { mutableStateOf(false) }
                val offsetY by animateDpAsState(
                    targetValue = if(screenState.isNavBarExpanded) 0.dp else 118.dp,
                    label = "",
                    finishedListener = {
                        if(!screenState.isNavBarExpanded) showNavBar = false
                    }
                )

                LaunchedEffect(key1 = screenState.isNavBarExpanded) {
                    if(screenState.isNavBarExpanded) showNavBar = true
                }

                var previousRoute by remember { mutableStateOf("") }
                Navigation(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 118.dp - offsetY),
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
                        CameraScreen(navController = navController)
                    }
                )

                if(showNavBar){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(118.dp)
                            .align(Alignment.BottomCenter)
                            .offset {
                                IntOffset(x = 0, y = with(this) { offsetY.toPx() }.toInt())
                            },
                        contentAlignment = Alignment.Center
                    ){
                        val convertIcon = painterResource(id = R.drawable.ic_convert)
                        val quizIcon = painterResource(id = R.drawable.ic_quiz)
                        val cameraIcon = Icons.Filled.PhotoCamera
                        NavigationBar(
                            items = listOf(
                                NavigationItem(
                                    itemLabel = stringResource(id = R.string.nav_convert),
                                    route = Screen.Convert.route,
                                    isSelected = navController.currentDestination?.route == Screen.Convert.route,
                                    painter = convertIcon,
                                    onItemClick = {
                                        previousRoute = navController.currentDestination?.route ?: ""
                                        if(navController.currentDestination?.route != Screen.Convert.route){
                                            navController.navigate(Screen.Convert.route){
                                                popUpTo(0)
                                            }
                                        }
                                    }
                                ),
                                NavigationItem(
                                    itemLabel = stringResource(id = R.string.nav_quiz),
                                    route = Screen.Quiz.route,
                                    isSelected = navController.currentDestination?.route == Screen.Quiz.route,
                                    painter = quizIcon,
                                    onItemClick = {
                                        previousRoute = navController.currentDestination?.route ?: ""
                                        if(navController.currentDestination?.route != Screen.Quiz.route){
                                            navController.navigate(Screen.Quiz.route){
                                                popUpTo(0)
                                            }
                                        }
                                    }
                                ),
                                NavigationItem(
                                    itemLabel = stringResource(id = R.string.nav_camera),
                                    route = Screen.Camera.route,
                                    isSelected = navController.currentDestination?.route == Screen.Camera.route,
                                    imageVector = cameraIcon,
                                    onItemClick = {
                                        previousRoute = navController.currentDestination?.route ?: ""
                                        if(navController.currentDestination?.route != Screen.Camera.route){
                                            navController.navigate(Screen.Camera.route){
                                                popUpTo(0)
                                            }
                                        }
                                    }
                                )
                            )
                        )
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
                },
                onLongClick = {
                    scope.launch {
                        context.intSettings.set("theme", null)
                        Toast.makeText(context, context.getString(R.string.toast_sys_theme), Toast.LENGTH_SHORT).show()
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
                        else -> {
                            if(!isSystemInDarkTheme()) Icons.Outlined.DarkMode
                            else Icons.Outlined.LightMode
                        }
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

            //blur when needed
            if(screenState.shouldBlur){
                Cloudy(radius = 25) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                onClick = { viewModel.blurBackground(false) },
                                indication = null,
                                interactionSource = remember {
                                    MutableInteractionSource()
                                }
                            )
                    )
                }
            }
        }
    }
}