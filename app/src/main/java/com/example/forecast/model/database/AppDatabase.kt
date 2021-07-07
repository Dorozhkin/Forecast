package com.example.forecast.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.forecast.model.pojo.MainPojoClass
import javax.inject.Singleton

@Singleton
@Database(entities = [MainPojoClass::class], version = 1 )
public abstract class AppDatabase: RoomDatabase() {

    public abstract fun getDao() : MainPojoClassDao
}