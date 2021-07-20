package com.example.forecast.di

import android.content.Context
import android.location.Geocoder
import androidx.room.Room
import com.example.forecast.model.database.AppDatabase
import com.example.forecast.model.database.MainPojoClassDao
import com.example.forecast.model.repository.WeatherRepository
import com.example.forecast.model.retrofit.RetrofitInterface
import com.example.forecast.model.suggestions.SuggestionsSource
import com.example.forecast.util.GeoUtil
import com.example.forecast.util.LocationProvider
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
class GeneralModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return  Room.databaseBuilder(context, AppDatabase::class.java, "database").build()
    }

    @Singleton
    @Provides
    fun provideDao(database: AppDatabase) : MainPojoClassDao {
        return database.getDao()
    }

    @Provides
    @Singleton
    fun provideRetrofitInterface(): RetrofitInterface {
        val scheme = "http"
        val host = "api.openweathermap.org"
        val path = "data/2.5/"

        val url = HttpUrl.Builder()
                .scheme(scheme)
                .host(host)
                .addPathSegments(path)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return retrofit.create(RetrofitInterface::class.java)
    }

    @Singleton
    @Provides
    fun getWeatherRepository(dao: MainPojoClassDao, retrofitInterface: RetrofitInterface): WeatherRepository {
        return WeatherRepository(dao, retrofitInterface)
    }

    @Singleton
    @Provides
    fun provideGeoUtil(context: Context): GeoUtil {
        val geocoder  = Geocoder(context, Locale.forLanguageTag("ru"))
        return  GeoUtil(geocoder, context)
    }

    @Singleton
    @Provides
    fun provideSuggestionsSource(context: Context): SuggestionsSource {
        return SuggestionsSource(context)
    }
}