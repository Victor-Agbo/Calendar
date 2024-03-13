package com.victor.calendar.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class Holiday(
    val name: String?,
    @SerialName(value = "name_local")
    val nameLocal: String?,
    val language: String?,
    val description: String?,
    val country: String?,
    val location: String?,
    val type: String?,
    val date: String?,
    @SerialName(value = "date_year")
    val dateYear: String?,
    @SerialName(value = "date_month")
    val dateMonth: String?,
    @SerialName(value = "date_day")
    val dateDay: String?,
    @SerialName(value = "week_day")
    val weekDay: String?
) : Parcelable
