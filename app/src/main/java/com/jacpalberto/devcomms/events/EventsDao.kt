package com.jacpalberto.devcomms.events

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.jacpalberto.devcomms.data.DevCommsEvent

/**
 * Created by Alberto Carrillo on 8/30/18.
 */
@Dao
interface EventsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(events: List<DevCommsEvent>)

    @Query("SELECT * FROM events")
    fun getList(): List<DevCommsEvent>

    @Query("DELETE FROM events")
    fun deleteAll()

    @Query("DELETE FROM events")
    fun deleteOuterElements()
}