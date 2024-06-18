package com.bogdan801.romanconverter.presentation.screens.quiz.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun QuizLostAd(
    modifier: Modifier = Modifier,
    adID: String = "ca-app-pub-3940256099942544/1033173712"
) {
    /*AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            // on below line specifying ad view.
            AdView(context).apply {
                // on below line specifying ad size
                //adSize = AdSize.BANNER
                // on below line specifying ad unit id
                // currently added a test ad unit id.
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-3940256099942544/1033173712"
                // calling load ad to load our ad.
                loadAd(AdRequest.Builder().build())
            }
        }
    )*/
}