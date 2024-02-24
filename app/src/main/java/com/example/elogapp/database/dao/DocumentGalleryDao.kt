package com.example.elogapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.elogapp.database.CURRENT_USER_ID
import com.example.elogapp.database.DocumentGallery
import com.example.elogapp.repository.responses.load.DocGalleryInfo


@Dao
interface DocumentGalleryDao {

    @Query("SELECT * FROM DOCUMENT_GALLERY where IS_SYNC IS 0")
    fun getDocGalleryDetails() : LiveData<List<DocGalleryInfo>>

    @Query("SELECT * FROM DOCUMENT_GALLERY")
    fun getDocGalleryDetails1() : List<DocGalleryInfo>

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert
    fun insertDocGallery(info : DocumentGallery) : Long

    @Query("SELECT * FROM DOCUMENT_GALLERY where uid = $CURRENT_USER_ID")
    fun getDocGallery() : LiveData<DocGalleryInfo>

    @Query("DELETE FROM DOCUMENT_GALLERY where row_id = :id")
    fun deleteDocGallery(id: String)

//    @Delete
//    fun deleteDocGallery(user: DocumentGallery)

    @Update
    fun updateDocGallery(user: DocumentGallery)
}