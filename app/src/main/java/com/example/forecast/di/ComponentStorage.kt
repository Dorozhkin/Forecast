package com.example.forecast.di

import android.app.Application

class ComponentStorage: Application() {
    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent
                .builder()
                .context(this)
                .buildAppComp()
    }
}