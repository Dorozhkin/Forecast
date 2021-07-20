package com.example.forecast.model.pojo

import androidx.room.*
import com.example.forecast.model.database.MultipleTypeConverter

@Entity
@TypeConverters(MultipleTypeConverter::class)
data class Forecast (

        //val lat : Double,
        //val lon : Double,
        //val timezone : String,
        @PrimaryKey
        var cityName: String,

        val timezone_offset : Int,

        @Embedded(prefix = "current")
        val current : Current,

        val hourly : List<Hourly>,
        val daily : List<Daily>
)
