package com.victor.calendar.ui.home

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.calendar.data.Event
import com.victor.calendar.data.OfflineEventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(offlineEventsRepository: OfflineEventsRepository) :
    ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

    }

    val homeUiState: StateFlow<HomeUiState> =
        offlineEventsRepository.getAllEventsStream().map { HomeUiState(eventList = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )
}

data class HomeUiState(
    val eventList: List<Event> = listOf(),
    val currentTime: Long = Calendar.getInstance().timeInMillis
)
