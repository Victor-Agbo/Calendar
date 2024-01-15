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

    private fun updateEventDetails(updateFunction: (EventDetails) -> EventDetails) {
        _uiState.update { currentState ->
            val updatedDetails = updateFunction(currentState.eventDetails)
            currentState.copy(eventDetails = updatedDetails, isEventValid = validateInput())
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


//    private fun updateProperties(transform: (EventUiState) -> EventUiState) {
//        _uiState.update { currentState ->
//            transform(currentState)
//        }
//    }
//
//    fun updateTitle(newTitle: String) {
//        val tempDetails = uiState.value.eventDetails
//        tempDetails.title = newTitle
//
//        updateProperties { currentState ->
//            currentState.copy(eventDetails = tempDetails, isEventValid = validateInput())
//        }
//    }
//
//    fun updateStart(newStart: Long) {
//        val tempDetails = uiState.value.eventDetails
//        tempDetails.start = newStart
//
//        updateProperties { currentState ->
//            currentState.copy(eventDetails = tempDetails, isEventValid = validateInput())
//        }
//    }
//
//    fun updateEnd(newEnd: Long) {
//        val tempDetails = uiState.value.eventDetails
//        tempDetails.end = newEnd
//
//        updateProperties { currentState ->
//            currentState.copy(eventDetails = tempDetails, isEventValid = validateInput())
//        }
//    }
//
//    fun updateDescription(newDescription: String) {
//        val tempDetails = uiState.value.eventDetails
//        tempDetails.description = newDescription
//        updateProperties { currentState ->
//            currentState.copy(eventDetails = tempDetails, isEventValid = validateInput())
//        }
//    }

    private fun validateInput(uiState: EventDetails = _uiState.value.eventDetails): Boolean {
        return with(uiState) {
            true
        }
    }
}
