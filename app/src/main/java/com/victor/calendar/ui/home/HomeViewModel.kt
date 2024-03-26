package com.victor.calendar.ui.home

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.snapshots.Snapshot.Companion.withMutableSnapshot
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.victor.calendar.data.Event
import com.victor.calendar.data.OfflineEventsRepository
import com.victor.calendar.util.MILLIS_IN_DAY
import com.victor.calendar.util.MILLIS_IN_WEEK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val offlineEventsRepository: OfflineEventsRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    @OptIn(SavedStateHandleSaveableApi::class)
    var startOfWeek: Long by savedStateHandle.saveable {
        mutableLongStateOf(Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis)
    }

    fun updateStartOfWeek(increase: Boolean = true) {
        withMutableSnapshot {
            startOfWeek += if (increase) MILLIS_IN_WEEK else -MILLIS_IN_WEEK
        }
    }

    private
    val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeEvents()
            Log.d("check", homeUiState.value.eventList[2].size.toString())
        }

    }

    fun observeEvents() {
        offlineEventsRepository.getWeekEventsStream(startTime = startOfWeek)
            .map {
                val sortedEventList = List(7) { mutableListOf<Event>() }
                for (event in it) {
                    val timeDifference = (event.start - startOfWeek) / MILLIS_IN_DAY
                    val index = max(0, min(6, timeDifference.toInt()))
                    sortedEventList[index].add(event)
                }
                HomeUiState(
                    sortedEventList,
                    startOfWeek
                )
            }
            .onEach { newState ->
                _homeUiState.value = newState
            }
            .launchIn(viewModelScope)
    }
}

data class HomeUiState(
    val eventList: List<MutableList<Event>> = List(7) { mutableListOf() },
    val startOfWeek: Long = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
) 
