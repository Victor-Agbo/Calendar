package com.victor.calendar.util

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.victor.calendar.data.monthAbbreviationMap
import org.jetbrains.annotations.VisibleForTesting
import java.util.Locale

@VisibleForTesting
fun convertMillisToFormattedTime(millis: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis

    val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
    return timeFormat.format(calendar.time)
}

fun convertMillisToFormattedDate(millis: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis

    val dateFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.US)
    return dateFormat.format(calendar.time)
}

fun extractMillsDay(millis: Long?): Int {
    return SimpleDateFormat("dd", Locale.getDefault()).format(millis).toInt()
}

fun extractMillsMonth(millis: Long?): Int {
    val month = SimpleDateFormat("MMM", Locale.getDefault()).format(millis).lowercase()
    return monthAbbreviationMap[month] ?: 11

}

fun extractMillsYear(millis: Long?): Int {
    return SimpleDateFormat("yyyy", Locale.getDefault()).format(millis).toInt()
}
