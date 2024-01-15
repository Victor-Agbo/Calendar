package com.victor.calendar.data

import kotlinx.coroutines.flow.Flow

interface EventsRepository {

    fun getAllEventsStream(): Flow<List<Event>>

    fun getEventStream(id: Int): Flow<Event?>

    suspend fun insertEvent(event: Event)

    suspend fun deleteEvent(event: Event)

    suspend fun updateEvent(event: Event)
}