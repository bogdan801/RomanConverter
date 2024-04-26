package com.bogdan801.romanconverter.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.presentation.navigation.Screen
import com.bogdan801.romanconverter.presentation.theme.navbarGradientBrush
import com.bogdan801.romanconverter.presentation.util.shadowCustom

data class NavigationItem(
    val itemLabel: String,
    val route: String = Screen.Convert.route,
    val isSelected: Boolean = false,
    val painter: Painter? = null,
    val imageVector: ImageVector? = null,
    val onItemClick: (route: String) -> Unit
)

@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    size: DpSize = DpSize(240.dp, 70.dp),
    cornerRadius: Dp = 20.dp,
    items: List<NavigationItem> = listOf()
) {
    Box(
        modifier = modifier
            .size(size)
            .shadowCustom(
                color = Color.Black.copy(alpha = 0.2f),
                blurRadius = 14.dp,
                shapeRadius = 20.dp,
                offsetY = 8.dp
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(brush = navbarGradientBrush()),
        contentAlignment = Alignment.Center
    ){
        Row(
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            items.forEach { item ->
                Column(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(
                            onClick = {
                                item.onItemClick(item.route)
                            }
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    val brush = Brush.verticalGradient(
                        colors = if(item.isSelected){
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.primary,
                            )
                        }
                        else {
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                Color.White,
                                MaterialTheme.colorScheme.primaryContainer,
                            )
                        }
                    )
                    val itemModifier = Modifier
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawRect(
                                    brush = brush,
                                    blendMode = BlendMode.SrcAtop
                                )
                            }
                        }

                    when {
                        item.painter != null -> {
                            Icon(
                                modifier = itemModifier,
                                painter = item.painter,
                                contentDescription = "${item.route} button"
                            )
                        }
                        item.imageVector != null -> {
                            Icon(
                                modifier = itemModifier,
                                imageVector = item.imageVector,
                                contentDescription = "${item.route} button"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.itemLabel,
                        style = MaterialTheme.typography.labelLarge,
                        color = if(item.isSelected) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.primaryContainer,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}