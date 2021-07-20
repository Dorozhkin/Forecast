package com.example.forecast.model.data_for_fragments

data class TomorrowGeneral (
        val timestamp: String,
        val dayTemp: String,
        val nightTemp: String,
        val icon: Int,
        val description: String,
        val probability: String,
        val color: Int
)