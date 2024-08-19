package com.bogdan801.romanconverter.presentation.util

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat

fun vibrateDevice(context: Context, duration: Long) {
    val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)

    vibrator?.let {
        if(it.hasVibrator()){
            val vibrationEffect = VibrationEffect.createOneShot(duration, 255)
            it.vibrate(vibrationEffect)
        }
    }
}