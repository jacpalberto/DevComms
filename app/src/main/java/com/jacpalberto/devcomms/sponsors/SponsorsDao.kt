package com.jacpalberto.devcomms.sponsors

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jacpalberto.devcomms.data.Sponsor
import io.reactivex.Single

/**
 * Created by Alberto Carrillo on 8/20/18.
 */
@Dao
interface SponsorsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(sponsor: List<Sponsor>)

    @Query("SELECT * FROM sponsors ORDER BY categoryPriority, name")
    fun getList(): Single<List<Sponsor>>

    @Query("DELETE FROM sponsors")
    fun deleteAll()
}