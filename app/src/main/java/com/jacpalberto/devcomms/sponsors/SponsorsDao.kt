package com.jacpalberto.devcomms.sponsors

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.jacpalberto.devcomms.data.Sponsor

/**
 * Created by Alberto Carrillo on 8/20/18.
 */
@Dao
interface SponsorsDao {
    @Insert(onConflict = REPLACE)
    fun save(sponsor: List<Sponsor?>)

    @Query("SELECT * FROM sponsors")
    fun getAll(): LiveData<List<Sponsor?>>

    @Query("DELETE FROM sponsors")
    fun deleteAll()
}