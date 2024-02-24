package com.example.elogapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.elogapp.database.DriverDutyInfo


@Dao
interface DriverDutyInfoDao {

    @Query("SELECT * FROM DRIVER_DUTY_INFO")
    fun getDriverList(): LiveData<List<DriverDutyInfo>>

//    @Query("SELECT * FROM CO_DRIVER where DName==:name")
//    suspend fun getDriverListByName(name: String): List<Driver>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDuty(driverDutyInfo: DriverDutyInfo)

//    @Insert
//    suspend fun insertAll(driver: Driver)
//
//    @Delete
//    suspend fun delete(driver: Driver)
//
//    @Update
//    suspend fun update(driver: Driver)
}
