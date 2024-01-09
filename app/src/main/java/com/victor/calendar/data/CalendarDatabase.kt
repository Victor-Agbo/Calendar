package com.victor.calendar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class CalendarDatabase : RoomDatabase() {

    abstract fun itemDao(): EventDao

    companion object {
        @Volatile
        private var Instance: CalendarDatabase? = null

        fun getDatabase(context: Context): CalendarDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CalendarDatabase::class.java, "event_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}