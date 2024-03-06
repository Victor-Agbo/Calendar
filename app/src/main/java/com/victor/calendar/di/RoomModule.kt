package com.victor.calendar.di

import android.content.Context
import androidx.room.Room
import com.victor.calendar.data.CalendarDatabase
import com.victor.calendar.data.CalendarDatabase.Companion.Instance
import com.victor.calendar.data.EventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun providesCalendarDatabase(@ApplicationContext context: Context): CalendarDatabase {
        return Instance ?: synchronized(this) {
            Room.databaseBuilder(context, CalendarDatabase::class.java, "event_database")
                .build()
                .also { Instance = it }
        }
    }

    @Provides
    fun providesCalendarDao(calendarDatabase: CalendarDatabase): EventDao {
        return calendarDatabase.eventDao()
    }
}