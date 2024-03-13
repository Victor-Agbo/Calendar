package com.victor.calendar.ui.holiday

import android.util.Log
import com.victor.calendar.data.Holiday
import com.victor.calendar.network.HolidayApi
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HolidayRepository @Inject constructor(private val holidayApi: HolidayApi) {
    suspend fun getHolidays(countryCode: String = Locale.getDefault().country): List<Holiday> {
        Log.d("iso", countryCode)
        return holidayApi.getHolidays(countryCode = countryCode)
    }
}