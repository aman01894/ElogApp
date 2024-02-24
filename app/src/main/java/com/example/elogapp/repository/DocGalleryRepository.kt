package com.example.elogapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.elogapp.database.DocumentGallery
import com.example.elogapp.database.MyRoomDb
import com.example.elogapp.database.Outbound
import com.example.elogapp.repository.responses.BaseResponse
import com.example.elogapp.repository.responses.load.DocGalleryInfo
import com.example.elogapp.repository.responses.load.DocLoadsResponse
import com.example.elogapp.repository.responses.load.LoadAcceptRejectResponse
import com.example.elogapp.repository.responses.load.LoadDispatchResponse
import com.example.elogapp.util.network.MyApi
import com.example.elogapp.util.network.SafeApiRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import retrofit2.Response

class DocGalleryRepository(
    private val api: MyApi,
    private val db: MyRoomDb) : SafeApiRequest() {


    fun getGalleryDataFromDb(): LiveData<List<DocGalleryInfo>> {

        return db.getDocGalleryDao().getDocGalleryDetails()

    }

    fun deleteGalleryDataFromDb(id: String)  {

        return db.getDocGalleryDao().deleteDocGallery(id)
    }

    suspend fun deleteImageFromServer(event: RequestBody): DocLoadsResponse {

        return apiRequest { api.deleteDocImageFromServer(event) }
    }

    suspend fun sendEventDataToServer(event: RequestBody): BaseResponse {

        return apiRequest { api.sendEventDataToServer(event) }
    }

    fun insertImageInGallery(info : DocumentGallery) : Long{

        return db.getDocGalleryDao().insertDocGallery(info)

    }

    fun getUser() = db.getUserDao().getUser()


    fun getLoadDocuments(loadID: Int) : MutableLiveData<DocLoadsResponse>{

        val data: MutableLiveData<DocLoadsResponse> = MutableLiveData<DocLoadsResponse>()

            CoroutineScope(Dispatchers.Default).launch {

                launch(Dispatchers.IO) {

                    var response = api?.getLoadDocuments(loadID)
                    withContext(Dispatchers.Default)
                    {
                        response?.let {
                            if (response.isSuccessful) {
                                data.postValue(response.body())

                            }

                        }
                    }

                }
            }

        return data
    }

    suspend fun getLoadDocs(loadID: Int) : DocLoadsResponse {

        return apiRequest { api.getLoadDocuments(loadID) }
    }

    /**
     * Insert Outbound Data in Outbound Table
     */
    fun insertOutboundData(data: Outbound) : Long{
        return db.getOutboundDao().insertOutboundData(data)
    }

    fun getOutboundList(isSynced: Boolean): List<Outbound> {
        return db.getOutboundDao().getOutboundList(isSynced)
    }

}