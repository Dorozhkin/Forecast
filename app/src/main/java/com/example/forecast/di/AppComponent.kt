package com.example.forecast.di

import android.content.Context
import com.example.forecast.model.database.ForecastDao
import com.example.forecast.model.repository.WeatherRepository
import com.example.forecast.model.retrofit.RetrofitInterface
import com.example.forecast.model.suggestions.SuggestionsSource
import com.example.forecast.util.GeoUtil
import com.example.forecast.viewmodels.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [GeneralModule::class])
interface AppComponent {

    fun getContext(): Context
    fun getViewModelFactory(): ViewModelFactory
    fun getMainPojoClassDao(): ForecastDao
    fun getRetrofitInterface(): RetrofitInterface
    fun getWeatherRepository(): WeatherRepository
    fun getGeoUtil(): GeoUtil
    fun getSuggestionsSource(): SuggestionsSource

    @Component.Builder
    interface CustomBuilder {
        fun buildAppComp() : AppComponent

        @BindsInstance
        fun context(context: Context): CustomBuilder

    }
}