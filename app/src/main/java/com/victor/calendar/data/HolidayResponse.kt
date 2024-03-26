package com.victor.calendar.data

data class HolidayResponse(
    val holidays: List<Holiday>,
    val status: String,
    val totalResults: Int
)
