package com.example.elogapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.elogapp.database.EventLog
import com.example.elogapp.database.Outbound


@Dao
interface OutboundDao {

    @Query("SELECT * FROM OUT_BOUND")
    fun getOutboundDataList(): LiveData<List<Outbound>>

    @Query("SELECT * FROM OUT_BOUND WHERE SYNCED=:isSynced")
    fun getOutboundList(isSynced: Boolean): List<Outbound>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOutboundData(data: Outbound) : Long

    @Query("UPDATE OUT_BOUND SET SYNCED=:synced WHERE id = :id")
    fun update(id: Int, synced: Boolean)

    @Delete
    fun delete(event: Outbound)

    @Update
    fun update(event: EventLog)


}
