package com.victor.calendar

import androidx.lifecycle.SavedStateHandle
import com.victor.calendar.data.Event
import com.victor.calendar.data.EventDao
import com.victor.calendar.data.OfflineEventsRepository
import com.victor.calendar.ui.home.HomeViewModel
import kotlinx.coroutines.flow.Flow
import org.junit.Test

class MyEventDao : EventDao {
    override suspend fun insert(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun update(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(event: Event) {
        TODO("Not yet implemented")
    }

    override fun getEvent(id: Int): Flow<Event> {
        TODO("Not yet implemented")
    }

    override fun getAllEvents(): Flow<List<Event>> {
        TODO("Not yet implemented")
    }

    override fun getWeekEvents(startTime: Long): Flow<List<Event>> {
        TODO("Not yet implemented")
    }

}

class GameViewModelTest {
    private val eventDao = MyEventDao()
    private val offlineEventsRepository = OfflineEventsRepository(eventDao)
    private val viewModel = HomeViewModel(offlineEventsRepository, SavedStateHandle())

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
    }
}