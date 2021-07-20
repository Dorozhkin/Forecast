package com.example.forecast.model.repository

import android.util.Log
import com.example.forecast.BuildConfig
import com.example.forecast.model.database.MainPojoClassDao
import com.example.forecast.model.pojo.Forecast
import com.example.forecast.model.retrofit.RetrofitInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

import kotlin.collections.HashMap

@Singleton
class WeatherRepository(private val dao: MainPojoClassDao, private val retrofit: RetrofitInterface) {
    private val error = "error"

    fun getData(cityName: String, lat: String, lon: String): Flow<Forecast> {
        return flow {
            if (cityName.isNotEmpty()) {
                Log.d("Repo1", "yes")
                if (dao.isRowExists(cityName)) {
                    Log.d("Repo2", "yes")
                    emit(dao.getByName(cityName))
                }
                if (lat != error && lon != error) {
                    Log.d("Repo3", "yes")
                    val coordinates = HashMap<String, String>()
                    coordinates["appid"] = BuildConfig.MYAPP_ID
                    coordinates["lat"] = lat
                    coordinates["lon"] = lon
                    val response = retrofit.getFromNetByCoordinates(coordinates)
                    if (response.isSuccessful) {
                        Log.d("Repo4", "yes")
                        val body = response.body()!!
                        body.cityName = cityName
                        dao.insert(body)
                        emit(body)
                    }
                }
            }
        }
    }
}