package com.victor.calendar.ui.event

import androidx.lifecycle.ViewModel
import com.victor.calendar.data.OfflineEventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private val offlineEventsRepository: OfflineEventsRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    fun resetEventDetails() {
        _uiState.update { currentState ->
            currentState.copy(eventDetails = EventDetails(), isEventValid = false)
        }
    }

    suspend fun saveEvent() {
        offlineEventsRepository.insertEvent(uiState.value.eventDetails.toEvent())
    }


    private fun updateEventDetails(updateFunction: (EventDetails) -> EventDetails) {
        _uiState.update { currentState ->
            val updatedDetails = updateFunction(currentState.eventDetails)
            currentState.copy(
                eventDetails = updatedDetails,
                isEventValid = validateInput(updatedDetails)
            )
        }
    }

    fun updateTitle(newTitle: String) {
        updateEventDetails { details ->
            details.copy(title = newTitle)
        }
    }

    fun updateStart(newStart: Long) {
        updateEventDetails { details ->
            details.copy(start = newStart)
        }
    }

    fun updateEnd(newEnd: Long) {
        updateEventDetails { details ->
            details.copy(end = newEnd)
        }
    }

    fun updateDescription(newDescription: String) {
        updateEventDetails { details ->
            details.copy(description = newDescription)
        }
    }

    fun updateValidity() {
        _uiState.update { currentState ->
            currentState.copy(isEventValid = validateInput())
        }
    }

    private fun validateInput(uiState: EventDetails = this.uiState.value.eventDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && start < end
        }
    }


}
