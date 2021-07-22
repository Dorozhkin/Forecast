package com.example.forecast.model.repository

import com.example.forecast.BuildConfig
import com.example.forecast.model.database.ForecastDao
import com.example.forecast.model.pojo.Forecast
import com.example.forecast.model.retrofit.RetrofitInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

import kotlin.collections.HashMap

@Singleton
class WeatherRepository(private val dao: ForecastDao, private val retrofit: RetrofitInterface) {
    private val error = "error"

    fun getData(cityName: String, lat: String, lon: String): Flow<Forecast> {
        return flow {
            if (cityName.isNotEmpty()) {
                if (dao.isRowExists(cityName)) {
                    emit(dao.getByName(cityName))
                }
                if (lat != error && lon != error) {
                    val coordinates = HashMap<String, String>()
                    coordinates["appid"] = BuildConfig.MYAPP_ID
                    coordinates["lat"] = lat
                    coordinates["lon"] = lon
                    val response = retrofit.getFromNetByCoordinates(coordinates)
                    if (response.isSuccessful) {
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