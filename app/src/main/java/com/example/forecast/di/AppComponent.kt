package com.example.forecast.di

import android.content.Context
import com.example.forecast.model.database.MainPojoClassDao
import com.example.forecast.model.repository.WeatherRepository
import com.example.forecast.model.retrofit.RetrofitInterface
import com.example.forecast.viewmodels.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [GeneralModule::class])
interface AppComponent {

    fun getContext(): Context
    fun getViewModelFactory(): ViewModelFactory
    fun getMainPojoClassDao(): MainPojoClassDao
    fun getRetrofitInterface(): RetrofitInterface
    fun getWeatherRepository(): WeatherRepository

    @Component.Builder
    interface CustomBuilder {
        fun buildAppComp() : AppComponent

        @BindsInstance
        fun context(context: Context): CustomBuilder

    }
}