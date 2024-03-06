package com.victor.calendar.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victor.calendar.data.OfflineEventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private val offlineEventsRepository: OfflineEventsRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()
    var initialEventEdit = EventDetails()

    fun resetEventDetails() {
        _uiState.update { currentState ->
            currentState.copy(eventDetails = EventDetails(), isEventValid = false)
        }
        initialEventEdit = EventDetails()
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

    fun getEvent(id: Int) {
        val fetchedEvent = EventDetails()
        viewModelScope.launch {
            initialEventEdit = fetchedEvent
            _uiState.update { currentState ->
                currentState.copy(
                    eventDetails = offlineEventsRepository.getEventStream(id)
                        .filterNotNull()
                        .first()
                        .toEventDetails()
                )
            }.also { initialEventEdit = uiState.value.eventDetails }
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

    private fun validateInput(uiState: EventDetails = this.uiState.value.eventDetails): Boolean {
        return with(uiState) {
            (title.isNotBlank() && start < end) && (initialEventEdit.title != title || initialEventEdit.start != start || initialEventEdit.end != end)
        }
    }
}
