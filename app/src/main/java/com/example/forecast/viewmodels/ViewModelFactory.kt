package com.example.forecast.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.model.repository.WeatherRepository
import com.example.forecast.model.suggestions.SuggestionsSource
import com.example.forecast.util.GeoUtil
import javax.inject.Inject

class ViewModelFactory @Inject constructor (private val repository: WeatherRepository, private val geoUtil: GeoUtil, private val suggestionsSource: SuggestionsSource) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SharedViewModel(repository, geoUtil, suggestionsSource) as T
    }
}