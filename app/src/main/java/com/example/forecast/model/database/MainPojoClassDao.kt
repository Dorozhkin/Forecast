package com.example.forecast.model.database

import androidx.room.*
import com.example.forecast.model.pojo.Forecast
import javax.inject.Singleton

@Singleton
@Dao
interface MainPojoClassDao {

    @Query("SELECT * FROM forecast WHERE cityName = :city")
    suspend fun getByName(city: String): Forecast

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mainPojoClass: Forecast)

    @Query("SELECT EXISTS(SELECT * FROM forecast WHERE cityName = :city)")
    suspend fun isRowExists(city: String): Boolean
}