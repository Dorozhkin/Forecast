package com.example.forecast.model.data_for_fragments

data class TodayGeneral (
        val timestamp: String,
        val dayTemp: String,
        val nightTemp: String,
        val currentTemp: String,
        val feelsTemp: String,
        val icon: String,
        val description: String,
        val probability: String,
        val color: Int

)