package com.victor.calendar.data

val months = listOf(
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
)

val monthAbbreviationMap: Map<String, Int> = months
    .associate { it.substring(0, 3).lowercase() to months.indexOf(it) }

fun getMonth(index: Int): String {
    return months[index]
}
