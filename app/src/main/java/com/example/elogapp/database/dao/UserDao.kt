package com.example.elogapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.elogapp.database.CURRENT_USER_ID
import com.example.elogapp.database.UserDetails


@Dao
interface UserDao {

    @Query("SELECT * FROM USER_DETAILS")
    fun getUserDetails() : LiveData<UserDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user : UserDetails) : Long

    @Query("SELECT * FROM USER_DETAILS where uid = $CURRENT_USER_ID")
    fun getUser() : LiveData<UserDetails>

    @Query("DELETE FROM USER_DETAILS where ID = :id")
    fun delete(id: Int)

    @Delete
    fun delete(user: UserDetails)

    @Update
    fun update(user: UserDetails)
}