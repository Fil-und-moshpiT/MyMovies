package com.filundmoshpit.mymovies.presentation

import android.app.Application
import android.content.Context
import com.filundmoshpit.mymovies.di.ApplicationContextComponent
import com.filundmoshpit.mymovies.di.DaggerApplicationContextComponent

//Dagger
class MainApplication : Application() {

    lateinit var contextComponent: ApplicationContextComponent

    override fun onCreate() {
        super.onCreate()

        contextComponent = DaggerApplicationContextComponent.builder().context(this).build()
    }
}

//Extension for context
val Context.contextComponent: ApplicationContextComponent
    get() = when (this) {
        is MainApplication -> contextComponent
        else -> this.applicationContext.contextComponent
    }