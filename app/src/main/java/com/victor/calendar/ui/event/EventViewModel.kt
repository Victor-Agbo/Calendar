package com.victor.calendar.ui.event

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor () : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private fun updateProperties(transform: (EventUiState) -> EventUiState) {
        _uiState.update { currentState ->
            transform(currentState)
        }
    }

    fun updateTitle(newTitle: String) {
        updateProperties { currentState ->
            currentState.copy(title = newTitle)
        }
    }

    fun updateStart(newStart: Long) {
        updateProperties { currentState ->
            currentState.copy(start = newStart)
        }
    }

    fun updateEnd(newEnd: Long) {
        updateProperties { currentState ->
            currentState.copy(end = newEnd)
        }
    }

    fun updateDescription(newDescription: String) {
        updateProperties { currentState ->
            currentState.copy(description = newDescription)
        }
    }
}
