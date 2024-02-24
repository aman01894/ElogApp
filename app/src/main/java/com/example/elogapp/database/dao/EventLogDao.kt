package com.example.elogapp.database.dao

import com.example.elogapp.database.EventLog
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.elogapp.repository.model.ChartInfo


@Dao
interface EventLogDao {

    //SELECT  CAST((JULIANDAY(datetime('now')) - JULIANDAY(datetime(TIME_STAMP)) )*86400 /60 AS INTEGER) AS minutes FROM EVENT_TIME_LOG where EVENT_TYPE='DRIVE'
    //SELECT datetime('now'),TIME_STAMP, (JULIANDAY(datetime('now')) - JULIANDAY(datetime(TIME_STAMP)) )*86400  AS differenceInSec FROM EVENT_TIME_LOG where EVENT_TYPE='DRIVE'
    @Query("SELECT * FROM EVENT_TIME_LOG")
    fun getEventLogList(): LiveData<List<EventLog>>

    @Query("SELECT * FROM EVENT_TIME_LOG ORDER BY ID DESC LIMIT 1")
    fun getEventLogById(): EventLog

    @Query("SELECT * FROM EVENT_TIME_LOG WHERE DUTY_ID=:dutyId AND EVENT_TYPE=:eventType")
    fun getAllEventByType(dutyId: Long, eventType : String): List<EventLog>

    @Query("SELECT * FROM EVENT_TIME_LOG WHERE SYNCED =:isSynced")
    fun getAllEvents(isSynced: Boolean): List<EventLog>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEventLog(event: EventLog)




//    @Query("UPDATE EVENT_TIME_LOG SET SYNCED=:synced, EVENT_END_TIME=:endTime, EVENT_DURATION=:totalTime WHERE id = :id")
//    fun update1(id: Int, endTime: Long?, totalTime: Long?, synced: Boolean)
//
//    @Query("UPDATE EVENT_TIME_LOG SET SYNCED=:synced, EVENT_END_TIME=:endTime, TIME_STAMP=:timeStamp, EVENT_DURATION=:totalTime WHERE DUTY_ID =:dutyId AND TIME_STAMP == -1")
//    fun update2(dutyId: Long?,endTime: Long?, totalTime: Long?, timeStamp: String, synced: Boolean)
//
//    @Query("UPDATE EVENT_TIME_LOG SET EVENT_END_TIME=:timeSEndServer, EVENT_DURATION=:duration,SYNCED=:synced, EVENT_END_TIME=:endTime, EVENT_DURATION=:totalTime WHERE EVENT_TYPE = :eventType AND DUTY_ID =:dutyId AND EVENT_END_TIME == -1")
//    fun update3(eventType: String, dutyId: Long?,endTime: Long?, totalTime: Long?, synced: Boolean, duration: String, timeSEndServer:String)//EVENT_TYPE = :eventType AND




   //@Query("SELECT Sum(MINS) DUTY_IN_MINS from(SELECT TIME_STAMP, datetime(TIME_STAMP/1000,'unixepoch') START, datetime(EVENT_END_TIME/1000,'unixepoch') 'END',  strftime('%s',datetime(EVENT_END_TIME/1000,'unixepoch')) - strftime('%s',datetime(TIME_STAMP/1000,'unixepoch')) SECONDS, (strftime('%s',datetime(TIME_STAMP/1000,'unixepoch')) - strftime('%s',datetime(TIME_STAMP/1000,'unixepoch') ))/60  MINS from EVENT_TIME_LOG where EVENT_TYPE=:eventType and EVENT_END_TIME!=-1 and DUTY_ID=:dutyId union SELECT TIME_STAMP, datetime(TIME_STAMP/1000,'unixepoch')   'START',  Current_time, strftime('%s','now') - strftime('%s',datetime(TIME_STAMP/1000,'unixepoch') )  'SECONDS', (strftime('%s','now') - strftime('%s',datetime(TIME_STAMP/1000,'unixepoch')))/60 MINS from EVENT_TIME_LOG where EVENT_TYPE=:eventType and EVENT_END_TIME=-1 and DUTY_ID =:dutyId)")
   @Query("select sum(A.DUTY_IN_MINS)  from (SELECT Sum(EVENT_DURATION) DUTY_IN_MINS from EVENT_TIME_LOG where EVENT_TYPE=:eventType and EVENT_END_TIME!='' and DUTY_ID=:dutyId union SELECT (JULIANDAY(DATETIME('now') ) -  JULIANDAY(DATETIME(TIME_STAMP) ) )*86400/60 DUTY_IN_MINS from EVENT_TIME_LOG where EVENT_TYPE=:eventType and EVENT_END_TIME='' and DUTY_ID=:dutyId ) AS A")
   fun getTotalEventTime(eventType: String, dutyId: Long?):Int

    @Query("UPDATE EVENT_TIME_LOG SET SYNCED=:synced WHERE id = :id")
    fun updateSyncStatus(id: Int, synced: Boolean)

    @Query("UPDATE EVENT_TIME_LOG SET EVENT_DURATION=((JULIANDAY(datetime(:eventEndTime)) - JULIANDAY(datetime(TIME_STAMP)))*86400/60), EVENT_END_TIME=:eventEndTime, SYNCED=:synced where EVENT_TYPE=:eventType AND DUTY_ID =:dutyId AND EVENT_END_TIME == ''")
    fun updateDutyEndTimeAndDuration(eventType: String?,dutyId: Long?,eventEndTime: String?, synced: Boolean)

    //@Query("SELECT EVENT_TYPE, TIME_STAMP from EVENT_TIME_LOG where DATE(TIME_STAMP)==DATE(:date)")
    //@Query("SELECT EVENT_TYPE, TIME_STAMP from EVENT_TIME_LOG")


    @Query("SELECT * FROM (SELECT ID , EVENT_TYPE,DATE(TIME_STAMP) DT, TIME_STAMP , EVENT_END_TIME from EVENT_TIME_LOG where DATE(TIME_STAMP)=DATE('now','-1 day') and ID=(Select MAX(ID) from EVENT_TIME_LOG where DATE(TIME_STAMP)=DATE('now','-1 day')) Union SELECT ID, EVENT_TYPE,DATE(TIME_STAMP) DT, TIME_STAMP , EVENT_END_TIME from EVENT_TIME_LOG where DATE(TIME_STAMP)=DATE('now')) ORDER BY ID")
    fun getChartData(): MutableList<ChartInfo>

    @Insert
    fun insertAll(event: EventLog)

    @Delete
    fun delete(event: EventLog)

    @Update
    fun update(event: EventLog)
}
