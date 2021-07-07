package com.example.forecast.model.data_for_fragments

data class HourDetail (
     val timeStamp: String,
     val description: String,
     val probability: String,
     val icon: String,
     val temp: String,
     val feelsTemp: String,
     val wind: String,
     val humidity: String,
     val precipitation: String
)