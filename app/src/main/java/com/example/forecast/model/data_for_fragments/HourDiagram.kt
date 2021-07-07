package com.example.forecast.model.data_for_fragments

data class HourDiagram (
        val timestamp: String,
        val temp: String,
        var leftMargin: Double = 0.0,
        val centerMargin: Double,
        var rightMargin: Double = 0.0,
        val tempStampMargin: Double
)