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

fun getMonth(index: Int): String {
    return months[index]
}
