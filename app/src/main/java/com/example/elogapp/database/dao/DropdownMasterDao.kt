package com.example.elogapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.elogapp.database.DropdownMaster
import com.example.elogapp.repository.model.Exceptions

@Dao
interface DropdownMasterDao {

    @Query("SELECT * FROM DROPDOWN_MASTER")
    fun getAllDropdownList(): LiveData<List<DropdownMaster>>

    @Query("SELECT * FROM DROPDOWN_MASTER where Type ==:type")
    fun getDropdownListByType(type: String): LiveData<List<DropdownMaster>>

    @Query("SELECT CODE, NAME FROM DROPDOWN_MASTER where Type ==:type")
    fun getExceptionList(type: String): LiveData<List<Exceptions>>

    @Insert
    fun insertDropdownList(dropdownList: DropdownMaster) {
    }

    @Delete
    fun delete(dropdownList: DropdownMaster)

    @Update
    fun update(dropdownList: DropdownMaster)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(dropdownList: List<DropdownMaster>)

    @Query("DELETE FROM DROPDOWN_MASTER")
    fun deleteAllDropdownList()


}
