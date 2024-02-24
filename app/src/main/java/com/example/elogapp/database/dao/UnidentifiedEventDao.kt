package com.example.elogapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.elogapp.database.UnidentifiedEvents


@Dao
interface UnidentifiedEventDao {

    @Query("SELECT * FROM UNIDENTIFIED_EVENTS WHERE CHECKED=:selectStatus ORDER BY ID")
    fun getAllUnidentifiedEventsByStatus(selectStatus: Boolean): List<UnidentifiedEvents>

    @Query("SELECT * FROM UNIDENTIFIED_EVENTS ORDER BY ID")
    fun getAllUnidentifiedEvents(): LiveData<List<UnidentifiedEvents>>


    @Query("SELECT * FROM UNIDENTIFIED_EVENTS WHERE SEQ=:seqId")
    fun getUnidentifiedEventById(seqId: Int): LiveData<UnidentifiedEvents>

    @Query("UPDATE UNIDENTIFIED_EVENTS SET CHECKED = :checked, User_id=:userId WHERE SEQ=:seqNo AND ID=:mId")
    fun updateUnidentifiedEventCheckedStatus(mId: Int, seqNo: Int, checked: Boolean, userId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEventLog(event: UnidentifiedEvents): Long

    @Query("UPDATE UNIDENTIFIED_EVENTS SET User_Id=:userId WHERE id = :id")
    fun updateUserId(userId: Int, id: Int)

    @Query("UPDATE UNIDENTIFIED_EVENTS SET SYNCED=:synced WHERE id = :id")
    fun updateSyncStatus(id: Int, synced: Boolean)

    @Insert
    fun insertAll(event: UnidentifiedEvents)

    @Delete
    fun delete(event: UnidentifiedEvents): Int

    @Update
    fun update(event: UnidentifiedEvents)
}
