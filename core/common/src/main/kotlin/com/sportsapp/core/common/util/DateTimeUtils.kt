package com.sportsapp.core.common.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeUtils {

    fun formatDate(date: String, inputFormat: String = Constants.DateFormats.API_DATE_FORMAT): String {
        return try {
            val inputFormatter = SimpleDateFormat(inputFormat, Locale.getDefault())
            val outputFormatter = SimpleDateFormat(Constants.DateFormats.DISPLAY_DATE_FORMAT, Locale.getDefault())
            val parsedDate = inputFormatter.parse(date)
            parsedDate?.let { outputFormatter.format(it) } ?: date
        } catch (e: Exception) {
            date
        }
    }

    fun getRelativeDate(daysOffset: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, daysOffset)
        val formatter = SimpleDateFormat(Constants.DateFormats.API_DATE_FORMAT, Locale.getDefault())
        return formatter.format(calendar.time)
    }

    fun getRelativeDateDisplay(daysOffset: Int): String {
        return when (daysOffset) {
            0 -> "Today"
            1 -> "Tomorrow"
            -1 -> "Yesterday"
            else -> {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, daysOffset)
                val formatter = SimpleDateFormat(Constants.DateFormats.DISPLAY_DATE_FORMAT, Locale.getDefault())
                formatter.format(calendar.time)
            }
        }
    }

    fun getCurrentDate(): String {
        val formatter = SimpleDateFormat(Constants.DateFormats.API_DATE_FORMAT, Locale.getDefault())
        return formatter.format(Date())
    }

    fun formatTime(time: String?): String {
        return time ?: "--:--"
    }
}