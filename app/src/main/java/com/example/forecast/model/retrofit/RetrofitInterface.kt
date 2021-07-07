package com.example.forecast.model.retrofit;

import com.example.forecast.model.pojo.MainPojoClass;


import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import javax.inject.Singleton

@Singleton
 interface RetrofitInterface {

    @GET("onecall?exclude=minutely,alerts&units=metric&lang=ru")
    suspend fun getFromNetByCoordinates(@QueryMap coordinates: Map<String, String>): MainPojoClass;
}
