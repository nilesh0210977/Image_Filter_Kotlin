package com.example.myapplication.utilities

import android.app.Application
import com.example.myapplication.dependencyinjection.repositoryModule
import com.example.myapplication.dependencyinjection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger


import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
@Suppress("unused")
class AppConfig:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger(Level.NONE)
            androidContext(this@AppConfig)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }
}