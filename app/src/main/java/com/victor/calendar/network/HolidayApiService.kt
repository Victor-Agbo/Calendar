package com.victor.calendar.network

import android.icu.util.Calendar
import com.victor.calendar.data.Holiday
import com.victor.calendar.util.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

val calendar: Calendar = Calendar.getInstance()

interface HolidayApi {

    @GET("v1/")
    suspend fun getHolidays(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("country") countryCode: String = "NG",
        @Query("day") day: String = calendar.get(Calendar.DAY_OF_MONTH).toString(),
        @Query("month") month: String = calendar.get(Calendar.MONTH).toString(),
        @Query("year") year: String = calendar.get(Calendar.YEAR).toString(),
    ): List<Holiday>
}


