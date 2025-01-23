package com.bogdan801.romanconverter.di

import android.app.Application
import com.google.android.gms.games.PlayGamesSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        PlayGamesSdk.initialize(this)
    }
}