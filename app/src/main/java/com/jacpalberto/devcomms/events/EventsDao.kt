package com.jacpalberto.devcomms.events

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jacpalberto.devcomms.data.DevCommsEvent

/**
 * Created by Alberto Carrillo on 8/30/18.
 */
@Dao
interface EventsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(events: List<DevCommsEvent>)

    @Query("SELECT * FROM events ORDER BY startTimeString, title")
    fun getList(): List<DevCommsEvent>

    @Query("SELECT * FROM events WHERE isFavorite = 1 ORDER BY startTimeString")
    fun getFavoriteList(): List<DevCommsEvent>

    @Query("DELETE FROM events")
    fun deleteAll()

    @Query("UPDATE events SET isFavorite = :isFavorite WHERE `key` =:key")
    fun updateFavorite(key: String, isFavorite: Boolean)
}