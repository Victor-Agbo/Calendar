package com.victor.calendar


import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.victor.calendar.data.CalendarDatabase
import com.victor.calendar.data.Event
import com.victor.calendar.data.EventDao
import com.victor.calendar.data.OfflineEventsRepository
import com.victor.calendar.di.RoomModule
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CalendarDatabaseTest {
    private lateinit var eventDao: EventDao
    private lateinit var db: CalendarDatabase
    private lateinit var roomModule: RoomModule
    private lateinit var offlineEventsRepository: OfflineEventsRepository

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        roomModule = RoomModule
        db = Room.inMemoryDatabaseBuilder(
            context, CalendarDatabase::class.java
        ).build()
        eventDao = RoomModule.providesCalendarDao(db)
        offlineEventsRepository = OfflineEventsRepository(eventDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val event = Event(1, "Sample Title", 1000000000L, 1000006000L)
        runBlocking {
            offlineEventsRepository.insertEvent(event)
            val events = offlineEventsRepository.getAllEventsStream()
            events.map {
                Assert.assertEquals(it[0], event)
            }

        }

    }
}