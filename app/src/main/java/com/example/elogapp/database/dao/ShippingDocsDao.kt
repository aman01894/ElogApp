package com.example.elogapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.elogapp.database.CURRENT_USER_ID
import com.example.elogapp.database.DocumentGallery
import com.example.elogapp.database.ShippingDocs
import com.example.elogapp.repository.responses.load.DocGalleryInfo
import com.example.elogapp.repository.responses.shippingdocs.ShippingDocsData


@Dao
interface ShippingDocsDao {

    @Query("SELECT * FROM SHIPPING_DOCUMENTS")
    fun getAllShippingDocList() : LiveData<List<ShippingDocs>>

    @Query("SELECT * FROM SHIPPING_DOCUMENTS WHERE IS_SYNC=:status")
    fun getShippingDocListByStatus(status: Boolean) : List<ShippingDocs>

    @Insert
    fun insertShippingDoc(docInfo : ShippingDocs) : Long

    @Query("DELETE FROM SHIPPING_DOCUMENTS where uid = :id")
    fun deleteDocGallery(id: String)

    @Delete
    fun deleteDocGallery(data: ShippingDocs)

    @Update
    fun updateDocGallery(user: ShippingDocs)
}