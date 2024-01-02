package com.victor.calendar.ui.event

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EventViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private fun updateTitle(newTitle: String) {
        _uiState.update { currentState ->
            currentState.copy(
                title = newTitle,
                start = currentState.start,
                end = currentState.end,
                description = currentState.description
            )
        }
    }

    private fun updateStart(newStart: Long) {
        _uiState.update { currentState ->
            currentState.copy(
                title = currentState.title,
                start = newStart,
                end = currentState.end,
                description = currentState.description
            )
        }
    }

    private fun updateEnd(newEnd: Long) {
        _uiState.update { currentState ->
            currentState.copy(
                title = currentState.title,
                start = currentState.start,
                end = newEnd,
                description = currentState.description
            )
        }
    }

    private fun updateDescription(newDescription: String) {
        _uiState.update { currentState ->
            currentState.copy(
                title = currentState.title,
                start = currentState.start,
                end = currentState.end,
                description = newDescription
            )
        }
    }

}