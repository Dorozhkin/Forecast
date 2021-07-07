package com.example.forecast.util

import com.example.forecast.R
import com.example.forecast.model.data_for_fragments.HourDetail
import com.example.forecast.model.data_for_fragments.HourDiagram
import com.example.forecast.model.data_for_fragments.TodayGeneral
import com.example.forecast.model.data_for_fragments.TomorrowGeneral
import com.example.forecast.model.pojo.Current
import com.example.forecast.model.pojo.Daily
import com.example.forecast.model.pojo.Hourly
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.*
import kotlin.math.roundToInt

object ClassConverter {
    private const val GRADUS_SIGN = "°"
    private const val BIG_GRADUS = "℃"
    private const val WIND_SIGN = "м/с"
    private const val PERCENT_SIGN = "%"
    private const val FEELS  = "Ощущается как "
    private const val COLON = ":"
    private const val COMMA = ", "
    private const val ZERO = "0"
    private const val ZEROS = "00"
    private const val TODAY = "сегодня"
    private const val WHITESPACE = " "
    private const val AMOUNT = " мм"

    fun createHourDiagramTomorrowList(dtNow: Int, entireList: List<Hourly>, timezoneOffset: Int): List<HourDiagram> {
        val hourNow = DiagramUtil.getHour(dtNow, timezoneOffset)
        val firstIndex = DiagramUtil.getLastIndexForTodayDiagram(hourNow) + 1
        val lastIndex = DiagramUtil.getLastIndexForTomorrowDiagram(firstIndex - 1)

        val maxTempInDiagrams = DiagramUtil.getMaxTempInDiagrams(entireList)
        val minTempInDiagrams = DiagramUtil.getMinTempInDiagrams(entireList)
        val delta = maxTempInDiagrams - minTempInDiagrams

        val answer =  mutableListOf<HourDiagram>()
        for (i in firstIndex..lastIndex) {
            val item = entireList[i]
            val timestamp = DiagramUtil.getHour(item.dt, timezoneOffset).toString() + COLON + ZEROS
            val temp = item.temp.toInt().toString()
            val centerMargin = DiagramUtil.getCenterMarginTop(item.temp.toInt(), maxTempInDiagrams, delta)
            val tempStampMargin = DiagramUtil.getTempStampMargin(centerMargin)

            val hourDiagram = HourDiagram(timestamp, temp, centerMargin = centerMargin, tempStampMargin = tempStampMargin)
            answer.add(hourDiagram)
        }
        for (index in answer.indices) {
            when(index) {
                0 -> {
                    val lastHourToday = entireList[firstIndex - 1]
                    val lastHourTodayTemp = lastHourToday.temp.toInt()
                    val lastHourTodayCenterMargin  = DiagramUtil.getCenterMarginTop(lastHourTodayTemp, maxTempInDiagrams, delta)

                    answer[index].leftMargin = (answer[index].centerMargin + lastHourTodayCenterMargin) / 2
                    answer[index].rightMargin = (answer[index].centerMargin + answer[index + 1].centerMargin) / 2
                }
                answer.lastIndex -> {
                    answer[index].leftMargin = (answer[index].centerMargin + answer[index - 1].centerMargin) / 2
                    answer[index].rightMargin = answer[index].centerMargin
                }
                else -> {
                    answer[index].leftMargin = (answer[index].centerMargin + answer[index - 1].centerMargin) / 2
                    answer[index].rightMargin = (answer[index].centerMargin + answer[index + 1].centerMargin) / 2
                }
            }
        }
        return answer
    }

    fun createHourDiagramTodayList(dtNow: Int, entireList: List<Hourly>, timezoneOffset: Int): List<HourDiagram> {
        val hourNow = DiagramUtil.getHour(dtNow, timezoneOffset)
        val lastIndex = DiagramUtil.getLastIndexForTodayDiagram(hourNow)

        val maxTempInDiagrams = DiagramUtil.getMaxTempInDiagrams(entireList)
        val minTempInDiagrams = DiagramUtil.getMinTempInDiagrams(entireList)
        val delta = maxTempInDiagrams - minTempInDiagrams

        val answer =  mutableListOf<HourDiagram>()
        for (i in 0..lastIndex) {
            val item = entireList[i]
            val timestamp = DiagramUtil.getHour(item.dt, timezoneOffset).toString() + COLON + ZEROS
            val temp = item.temp.toInt().toString()
            val centerMargin = DiagramUtil.getCenterMarginTop(item.temp.toInt(), maxTempInDiagrams, delta)
            val tempStampMargin = DiagramUtil.getTempStampMargin(centerMargin)

            val hourDiagram = HourDiagram(timestamp, temp, centerMargin = centerMargin, tempStampMargin = tempStampMargin)
            answer.add(hourDiagram)
        }

        for (index in answer.indices) {
            when (index) {
                0 -> {
                    answer[index].leftMargin = answer[index].centerMargin
                    answer[index].rightMargin = (answer[index].centerMargin + answer[index + 1].centerMargin) / 2
                }
                answer.lastIndex -> {
                    val firstHourTomorrow = entireList[lastIndex + 1]
                    val firstHourTomorrowTemp = firstHourTomorrow.temp.toInt()
                    val firstHourTomorrowCenterMargin = DiagramUtil.getCenterMarginTop(firstHourTomorrowTemp, maxTempInDiagrams, delta)

                    answer[index].leftMargin = (answer[index].centerMargin + answer[index - 1].centerMargin) / 2
                    answer[index].rightMargin = (answer[index].centerMargin + firstHourTomorrowCenterMargin) / 2
                }
                else -> {
                    answer[index].leftMargin = (answer[index].centerMargin + answer[index - 1].centerMargin) / 2
                    answer[index].rightMargin = (answer[index].centerMargin + answer[index + 1].centerMargin) / 2
                }
            }
        }
        return answer
    }

    fun createHoursDetailList(input: List<Hourly>, timezoneOffset: Int): List<HourDetail> {
        val answer = mutableListOf<HourDetail>()
        for (item in input) {
            val forDate = StringBuilder()

            val dateNow = LocalDate.now()
            val instant = Instant.ofEpochSecond(item.dt.toLong() + timezoneOffset.toLong())
            val timeFromJS = instant.atOffset(ZoneOffset.UTC).toLocalTime()
            val dateFromJS = instant.atOffset(ZoneOffset.UTC).toLocalDate()

            if (dateFromJS.dayOfYear == dateNow.dayOfYear) {
                forDate.append(TODAY).append(COMMA)
            } else {
                forDate.append(dateFromJS.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"))).append(COMMA)
                forDate.append(dateFromJS.dayOfMonth).append(WHITESPACE)
                forDate.append(dateFromJS.month.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"))).append(COMMA)
            }
            forDate.append(timeFromJS.hour).append(COLON).append(ZEROS)

            val timestamp = forDate.toString()
            val desc = item.weather[0].description
            val description = desc.substring(0,1).toUpperCase() + desc.substring(1)
            val pop = (item.pop*100).toInt()
            val probability =  if (pop == 0) "" else pop.toString() + PERCENT_SIGN
            val icon = item.weather[0].icon
            val temp = item.temp.toInt().toString() + BIG_GRADUS
            val feelsTemp = item.feels_like.toInt().toString() + BIG_GRADUS
            val wind = item.wind_speed.toInt().toString() + WIND_SIGN
            val humidity = item.humidity.toString() + PERCENT_SIGN

            var precipitation = ""
            if (item.rain != null || item.snow != null) {
                var sum = 0.0
                if (item.rain != null) {
                    sum += item.rain.oneH
                }
                if (item.snow != null) {
                    sum += item.snow.oneH
                }
                precipitation = sum.toInt().toString() + AMOUNT
            } else {
                precipitation = ZERO + AMOUNT
            }
            answer.add(HourDetail(timestamp, description, probability, icon, temp, feelsTemp, wind, humidity, precipitation))
        }
        return answer
    }

    fun createTodayGeneral(current: Current, today: Daily, timezoneOffset: Int): TodayGeneral {
        val timestamp = todayTimeStampCreator(timezoneOffset, current.dt)
        val dayTemp = today.temp.day.roundToInt().toString() + GRADUS_SIGN
        val nightTemp = today.temp.night.roundToInt().toString() + GRADUS_SIGN
        val currentTemp = current.temp.roundToInt().toString()
        val feelsTemp = FEELS + current.feels_like.roundToInt().toString() + GRADUS_SIGN
        val icon = current.weather[0].icon
        val desc = current.weather[0].description
        val description = desc.substring(0, 1).toUpperCase() + desc.substring(1)
        val probability  = (today.pop * 100).toString().replace("^0[.]".toRegex(), "").replace("[.][0-9]+".toRegex(), "") + PERCENT_SIGN

        val sunrise = current.sunrise
        val sunset  = current.sunset
        val timeNow = current.dt
        val code = current.weather[0].id
        val color = pickColor(sunrise, sunset, timeNow, code)

        return TodayGeneral(timestamp, dayTemp, nightTemp, currentTemp, feelsTemp, icon, description, probability, color)
    }

    fun createTomorrowGeneral(tomorrow: Daily, timezoneOffset: Int): TomorrowGeneral {
        val timestamp = tomorrowTimestampCreator(timezoneOffset, tomorrow.dt)
        val dayTemp = tomorrow.temp.day.roundToInt().toString() + GRADUS_SIGN
        val nightTemp = tomorrow.temp.night.roundToInt().toString() + GRADUS_SIGN
        val icon = tomorrow.weather[0].icon
        val desc = tomorrow.weather[0].description
        val description = desc.substring(0, 1).toUpperCase() + desc.substring(1)
        val probability  = (tomorrow.pop * 100).toString().replace("^0[.]".toRegex(), "").replace("[.][0-9]+".toRegex(), "") + PERCENT_SIGN

        val code = tomorrow.weather[0].id
        val color = pickColor(code = code)

        return TomorrowGeneral(timestamp, dayTemp, nightTemp, icon, description, probability, color)
    }

     private fun pickColor(sunrise: Int = 0, sunset: Int = 0, timeNow: Int = 0, code: Int): Int {
        var color: Int = R.color.orange
        if (timeNow > sunset || timeNow < sunrise) {
            color = R.color.night
        } else {
            when (code / 100) {
                2 -> color = R.color.thunderstorm
                3 -> color = R.color.drizzle
                5 -> color = R.color.rain
                6 -> color = R.color.darkGrey
                7 -> color = R.color.atmosphere
                8 ->  {
                    color = when(code) {
                        800 -> R.color.clear
                        else -> R.color.drizzle
                    }
                }
            }
        }
        return color
    }

    private fun todayTimeStampCreator(timezoneOffset: Int, dt: Int): String {
        val instant = Instant.ofEpochSecond(dt.toLong() + timezoneOffset.toLong())
        val timeFromJS = instant.atOffset(ZoneOffset.UTC).toLocalTime()
        val dateFromJS = instant.atOffset(ZoneOffset.UTC).toLocalDate()

        val day = dateFromJS.dayOfMonth
        val month = dateFromJS.month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"))
        val hour = timeFromJS.hour
        val minute = timeFromJS.minute
        return String.format("%d %s, %d:%02d", day, month, hour, minute)
    }

    private fun tomorrowTimestampCreator(timezoneOffset: Int, dt: Int): String {
        val forDate =  StringBuilder()
        val instant = Instant.ofEpochSecond(dt.toLong() + timezoneOffset.toLong())
        val dateFromJS = instant.atOffset(ZoneOffset.UTC).toLocalDate()
        forDate.append(dateFromJS.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"))).append(", ")
        forDate.append(dateFromJS.dayOfMonth).append(" ")
        forDate.append(dateFromJS.month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru")))
        return forDate.toString()
    }

}