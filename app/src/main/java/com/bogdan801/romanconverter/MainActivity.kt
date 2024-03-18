package com.bogdan801.romanconverter

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.FlashlightOn
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.bogdan801.romanconverter.ui.theme.RomanCalculatorTheme
import com.bogdan801.util_library.getCurrentDateTime
import com.bogdan801.util_library.intSettings
import com.bogdan801.util_library.toFormattedTime
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )
        setContent {
            RomanCalculatorTheme {
                val currentTheme = intSettings["theme"].collectAsState(initial = 2)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "mdcccxcvii",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.displayLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            lifecycleScope.launch {
                                intSettings.set("theme", ((currentTheme.value ?: 0) + 1) % 3)
                            }
                        }
                    ) {
                        Text(
                            text = when(currentTheme.value){
                                0 -> "Світла"
                                1 -> "Темна"
                                else -> "Авто"
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                        Icon(imageVector = Icons.Filled.PhotoCamera, contentDescription = "")
                        Icon(painterResource(id = R.drawable.ic_auto_theme), contentDescription = "")
                        Icon(imageVector = Icons.Outlined.DarkMode, contentDescription = "")
                        Icon(imageVector = Icons.Filled.Pause, contentDescription = "")
                        Icon(imageVector = Icons.Outlined.FlashlightOn, contentDescription = "")
                        Icon(imageVector = Icons.Outlined.CheckCircle, contentDescription = "")
                        Icon(imageVector = Icons.AutoMirrored.Outlined.Backspace, contentDescription = "")
                    }

                }
            }
        }
    }
}