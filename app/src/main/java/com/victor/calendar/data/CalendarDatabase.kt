package com.victor.calendar.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class CalendarDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        var Instance: CalendarDatabase? = null
    }
}