package com.victor.calendar.ui.event

import com.victor.calendar.data.Event

data class EventDetails(
    val id: Int = 0,
    var title: String = "",
    var start: Long = 0L,
    var end: Long = 0L,
    var description: String = ""

)

fun EventDetails.toEvent() = Event(
    title = title,
    start = start,
    end = end,
    description = description,
)

fun Event.toEventUiState(isEntryValid: Boolean): EventUiState =
    EventUiState(eventDetails = this.toEventDetails(), isEventValid = isEntryValid)

fun Event.toEventDetails(): EventDetails = EventDetails(
    id = id,
    title = title,
    start = start,
    end = end,
    description = description,
)