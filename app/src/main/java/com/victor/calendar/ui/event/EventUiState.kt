package com.victor.calendar.ui.event

data class EventUiState(
    val eventDetails: EventDetails = EventDetails(),
    val isEventValid: Boolean = false
)
