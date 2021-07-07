package com.example.forecast.model.database

import androidx.room.*
import com.example.forecast.model.pojo.MainPojoClass
import javax.inject.Singleton

@Singleton
@Dao
interface MainPojoClassDao {

    @Query("SELECT * FROM mainpojoclass WHERE cityName = :city")
    suspend fun getByName(city: String): MainPojoClass

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mainPojoClass: MainPojoClass)

    @Query("SELECT EXISTS(SELECT * FROM mainpojoclass WHERE cityName = :city)")
    suspend fun isRowExists(city: String): Boolean
}