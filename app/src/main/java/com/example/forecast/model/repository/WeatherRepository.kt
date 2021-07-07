package com.example.forecast.model.repository

import com.example.forecast.BuildConfig
import com.example.forecast.model.database.MainPojoClassDao
import com.example.forecast.model.pojo.MainPojoClass
import com.example.forecast.model.retrofit.RetrofitInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

import kotlin.collections.HashMap

@Singleton
class WeatherRepository(private val dao: MainPojoClassDao, private val retrofit: RetrofitInterface) {

    fun getData(cityName: String, lat: String, lon: String): Flow<MainPojoClass> {
        return flow {
            if (cityName.isNotEmpty()) {
                if (dao.isRowExists(cityName)) {
                    emit(dao.getByName(cityName))
                }
                val coordinates = HashMap<String, String>()
                coordinates["appid"] = BuildConfig.MYAPP_ID
                coordinates["lat"] = lat
                coordinates["lon"] = lon
                val response = retrofit.getFromNetByCoordinates(coordinates)
                response.cityName = cityName
                dao.insert(response)
                emit(response)
            }
        }
    }
}