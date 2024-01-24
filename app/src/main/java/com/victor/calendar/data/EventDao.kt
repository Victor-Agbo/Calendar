package com.victor.calendar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.victor.calendar.util.MILLIS_IN_WEEK
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("SELECT * from events WHERE id = :id")
    fun getEvent(id: Int): Flow<Event>

    @Query("SELECT * from events ORDER BY start ASC")
    fun getAllEvents(): Flow<List<Event>>

    @Query("SELECT * from events WHERE start < :startTime AND `end` < :startTime+${MILLIS_IN_WEEK} ORDER BY start ASC")
    fun getWeekEvents(startTime: Long): Flow<List<Event>>


}