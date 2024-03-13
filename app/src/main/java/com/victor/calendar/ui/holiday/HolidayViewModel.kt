package com.victor.calendar.ui.holiday

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.calendar.data.Holiday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface HolidayUiState {
    data class Success(val holidayList: List<Holiday>) : HolidayUiState
    data object Error : HolidayUiState
    data object Loading : HolidayUiState
}

@HiltViewModel
class HolidayViewModel @Inject constructor(private val holidayRepository: HolidayRepository) :
    ViewModel() {
    var holidayUiState: HolidayUiState by mutableStateOf(HolidayUiState.Loading)
        private set

    init {
        getHolidays()
    }

    fun getHolidays(countryCode: String = "IN") {
        Log.d("iso", countryCode)
        viewModelScope.launch {
            holidayUiState = HolidayUiState.Loading
            holidayUiState = try {
                HolidayUiState.Success(holidayRepository.getHolidays(countryCode))
            } catch (e: IOException) {
                HolidayUiState.Error
            } catch (e: HttpException) {
                HolidayUiState.Error
            }
        }
    }

}
