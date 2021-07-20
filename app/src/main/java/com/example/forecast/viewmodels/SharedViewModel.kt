package com.example.forecast.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.forecast.model.repository.WeatherRepository
import com.example.forecast.model.suggestions.SuggestionsSource
import com.example.forecast.util.ClassConverter
import com.example.forecast.util.GeoUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*


class SharedViewModel(private val repository: WeatherRepository, private val geoUtil: GeoUtil, private val suggestionsSource: SuggestionsSource): ViewModel() {

    val queryForSuggestions = MutableStateFlow("")
    val enteredCityName = MutableSharedFlow<String>(1)
    val correctCityName = MutableLiveData<String>()
    private val forCityFlow = enteredCityName.flatMapLatest {
        var lat = ""
        var lon = ""
        var correctName = ""

        if  (it.isNotEmpty()) {
            lat = geoUtil.determineLat(it)
            lon = geoUtil.determineLon(it)
            correctName = geoUtil.determineCorrectName(it)
            correctCityName.postValue(correctName)

        }
        repository.getData(correctName, lat, lon)
    }

    val suggestionsFlow = queryForSuggestions.flatMapLatest {
        suggestionsSource.retrieveSuggestionsList(it)
    }.asLiveData(Dispatchers.IO)

    val todayGeneralFlow = forCityFlow.map {
        val current = it.current
        val today = it.daily[0]
        val timezoneOffset = it.timezone_offset
        ClassConverter.createTodayGeneral(current, today, timezoneOffset)
    }.asLiveData(Dispatchers.IO)

    val tomorrowGeneralFlow = forCityFlow.map {
        val tomorrow = it.daily[1]
        val timezoneOffset = it.timezone_offset
        ClassConverter.createTomorrowGeneral(tomorrow, timezoneOffset)
    }.asLiveData(Dispatchers.IO)

    val hourDetailFlow = forCityFlow.map {
        val hours = it.hourly
        val timezoneOffset = it.timezone_offset
        ClassConverter.createHoursDetailList(hours, timezoneOffset)
    }.asLiveData(Dispatchers.IO)

    val hoursDiagramTodayFlow = forCityFlow.map {
        val dtNow = it.current.dt
        val hours = it.hourly
        val timezoneOffset = it.timezone_offset
        ClassConverter.createHourDiagramTodayList(dtNow, hours, timezoneOffset)
    }.asLiveData(Dispatchers.IO)

    val hoursDiagramTomorrowFlow = forCityFlow.map {
        val dtNow = it.current.dt
        val hours = it.hourly
        val timezoneOffset = it.timezone_offset
        ClassConverter.createHourDiagramTomorrowList(dtNow, hours, timezoneOffset)
    }.asLiveData(Dispatchers.IO)

    suspend fun swipeRefresh(query: String) {
        enteredCityName.emit(query)
    }

    suspend fun initialStart() {
        enteredCityName.emit(geoUtil.determineCityInitial())
    }
}

