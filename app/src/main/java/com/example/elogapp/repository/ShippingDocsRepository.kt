package com.example.elogapp.repository

import androidx.lifecycle.LiveData
import com.example.elogapp.database.MyRoomDb
import com.example.elogapp.database.ShippingDocs
import com.example.elogapp.database.UserDetails
import com.example.elogapp.util.network.MyApi
import com.example.elogapp.util.network.SafeApiRequest
import com.example.elogapp.repository.responses.master.MasterResponse
import com.example.elogapp.repository.responses.predvir.PreDvirTripResponse
import com.example.elogapp.repository.responses.shippingdocs.ShippingDocResponse
import com.example.elogapp.repository.responses.shippingdocs.ShippingDocsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

class ShippingDocsRepository(
    private val api: MyApi,
    private val db: MyRoomDb
) : SafeApiRequest() {



    /**
     * Get Shipping Docs List
     */
    suspend fun getShippingDocListFromServer(): ShippingDocResponse {

        return apiRequest { api.getShippingDocList() }

    }

    /**
     * Get Shipping Doc List from DB
     */
    fun getShippingDocsList(): LiveData<List<ShippingDocs>> {

        return db.getShippingDocsDao().getAllShippingDocList()
    }

    /**
     * INsert Doc No in Shipping Doc Table DB
     */
    fun insertShippingDoc(docInfo : ShippingDocs): Long {

        return db.getShippingDocsDao().insertShippingDoc(docInfo)
    }


    /**
     * Get the User Details From DB
     */
    fun getUserDetails (): LiveData<UserDetails> {
        return db.getUserDao().getUserDetails()
    }

}