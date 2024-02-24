package com.example.elogapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.elogapp.database.Violation


@Dao
interface ViolationDao {

    @Query("SELECT * FROM VIOLATION_TABLE WHERE DUTY_ID=:violationType AND VIOLATION_TYPE=:dutyId")
    fun selectViolationByTypeAndDutyId(violationType: String, dutyId: Int): LiveData<List<Violation>>

    @Query("SELECT * FROM VIOLATION_TABLE")
    fun getViolationList(): List<Violation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertViolation(data: Violation)

    @Query("UPDATE VIOLATION_TABLE SET SYNCED=:synced WHERE DUTY_ID = :dutyId AND VIOLATION_TYPE=:violationType")
    fun update(violationType: String, dutyId: Int, synced: Boolean)

    @Delete
    fun delete(violation: Violation)

    @Update
    fun update(violation: Violation)


}
