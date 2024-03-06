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
    private val _eventUiState = MutableStateFlow(EventUiState())
    val eventUiState: StateFlow<EventUiState> = _eventUiState.asStateFlow()
    var initialEventEdit = EventDetails()

    fun resetEventDetails() {
        _eventUiState.update { currentState ->
            currentState.copy(eventDetails = EventDetails(), isEventValid = false)
        }
        initialEventEdit = EventDetails()
    }

    suspend fun deleteEvent() {
        offlineEventsRepository.deleteEvent(eventUiState.value.eventDetails.toEvent())
    }

    suspend fun saveEvent() {
        offlineEventsRepository.insertEvent(eventUiState.value.eventDetails.toEvent())
    }

    suspend fun updateEvent() {
        offlineEventsRepository.updateEvent(eventUiState.value.eventDetails.toEvent())

    }

    private fun updateEventDetails(updateFunction: (EventDetails) -> EventDetails) {
        _eventUiState.update { currentState ->
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
            _eventUiState.update { currentState ->
                currentState.copy(
                    eventDetails = offlineEventsRepository.getEventStream(id)
                        .filterNotNull()
                        .first()
                        .toEventDetails()
                )
            }.also { initialEventEdit = eventUiState.value.eventDetails }
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

    private fun validateInput(eventUiState: EventDetails = this.eventUiState.value.eventDetails): Boolean {
        return with(eventUiState) {
            (title.isNotBlank() && start < end) && (initialEventEdit.title != title || initialEventEdit.start != start || initialEventEdit.end != end)
        }
    }
}
