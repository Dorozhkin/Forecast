package com.example.forecast.util

import com.example.forecast.model.pojo.Hourly
import java.time.Instant
import java.time.ZoneOffset
import java.util.*

object DiagramUtil {
    const val TOP_PADDING = 0.17
    const val BOTTOM_PADDING = 0.17

    fun getHour(dt: Int, offset: Int): Int {
        val instant = Instant.ofEpochSecond(dt + offset.toLong())
        val timeFromJS = instant.atOffset(ZoneOffset.UTC).toLocalTime()
        return timeFromJS.hour;
    }

    fun getLastIndexForTodayDiagram(hour: Int): Int {
        return 29 - hour
    }

    fun getLastIndexForTomorrowDiagram(lastIndexForToday: Int): Int {
         return if (lastIndexForToday <= 24) lastIndexForToday + 24 else 47
    }

    fun getMaxTempInDiagrams(hours: List<Hourly>): Int {
        val temp = mutableListOf<Int>()
        for(hour in hours) {
            temp.add(hour.temp.toInt())
        }
        return Collections.max(temp)
    }

    fun getMinTempInDiagrams(hours: List<Hourly>): Int {
        val temp = mutableListOf<Int>()
        for(hour in hours) {
            temp.add(hour.temp.toInt())
        }
        return Collections.min(temp)
    }

    fun getCenterMarginTop(temp: Int, maxTempInDiagrams: Int, deltaInDiagrams: Int): Double {
        return TOP_PADDING + (1 - TOP_PADDING - BOTTOM_PADDING)*(maxTempInDiagrams - temp)/deltaInDiagrams
    }

    fun getTempStampMargin(centerMarginTop: Double): Double {
        return centerMarginTop - TOP_PADDING
    }
}