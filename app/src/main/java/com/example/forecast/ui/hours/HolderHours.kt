package com.example.forecast.ui.hours

import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.R
import com.example.forecast.databinding.HolderHoursBinding
import com.example.forecast.model.data_for_fragments.HourDetail


class HolderHours(val binding: HolderHoursBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setValues(hour: HourDetail) {
        binding.timeStamp.text = hour.timeStamp
        binding.description.text = hour.description
        binding.probability.text = hour.probability
        binding.temperature.text = hour.temp
        binding.feelsLike.text = hour.feelsTemp
        binding.wind.text = hour.feelsTemp
        binding.humidity.text = hour.humidity
        binding.precipitation.text = hour.precipitation
        binding.image.setImageResource(hour.icon)
        val i = hour.icon
        if (i == R.drawable.p01d || i == R.drawable.p01n || i == R.drawable.p09d || i == R.drawable.p09n || i == R.drawable.p11d || i == R.drawable.p11n) {
            binding.image.setPadding(0, 25, 0, 25)
        }
    }
}