package com.example.forecast.model.database

import androidx.room.TypeConverter
import com.example.forecast.model.pojo.Daily
import com.example.forecast.model.pojo.Hourly
import com.example.forecast.model.pojo.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MultipleTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun listWeatherToString(list: List<Weather>): String {
        val type = object: TypeToken<List<Weather>>() {}.type
        return gson.toJson(list, type)
    }
    @TypeConverter
    fun stringToListWeather(json: String): List<Weather> {
        val type = object: TypeToken<List<Weather>>() {}.type
        return gson.fromJson(json, type)
    }
    @TypeConverter
    fun  listHourlyToString(list: List<Hourly>): String {
        val type = object: TypeToken<List<Hourly>>() {}.type
        return gson.toJson(list, type)
    }
    @TypeConverter
    fun stringToListHourly(json: String): List<Hourly> {
        val type = object: TypeToken<List<Hourly>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun  listDailyToString(list: List<Daily>): String {
        val type = object: TypeToken<List<Daily>>() {}.type
        return gson.toJson(list, type)
    }
    @TypeConverter
    fun stringToListDaily(json: String): List<Daily> {
        val type = object: TypeToken<List<Daily>>() {}.type
        return gson.fromJson(json, type)
    }
}