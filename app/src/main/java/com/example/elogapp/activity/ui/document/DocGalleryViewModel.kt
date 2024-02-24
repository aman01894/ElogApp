package com.example.elogapp.activity.ui.document

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elogapp.database.DocumentGallery
import com.example.elogapp.database.Outbound
import com.example.elogapp.repository.responses.BaseResponse
import com.example.elogapp.repository.responses.load.*
import com.example.elogapp.util.ApiException
import com.example.elogapp.util.Coroutines
import com.example.elogapp.util.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import com.example.elogapp.repository.DocGalleryRepository


class DocGalleryViewModel(private val repository: DocGalleryRepository) : ViewModel() {

    var loadListener: DocLoadListener? = null

    suspend fun sendOutBondDataToServer(eventLog : RequestBody) : BaseResponse {

        return  repository.sendEventDataToServer(eventLog)

    }

    fun getUserDetail() = repository.getUser()

    fun getLoadDocs(loadId: Int, apiCode: Int)  {

        loadListener?.onStarted()
        Coroutines.mainIO {
            try {
                val authResponse = repository.getLoadDocs(loadId)
                authResponse?.let {
                    loadListener?.onSuccess(authResponse, apiCode)
                    return@mainIO
                }
                loadListener?.onFailure(authResponse.message!!, apiCode)

            } catch (e: ApiException) {
                loadListener?.onFailure(e.message!!, apiCode)
            } catch (e: NoInternetException) {
                loadListener?.onFailure(e.message!!, apiCode)
            }
        }
    }

    fun getGalleryDataFromDb() : LiveData<List<DocGalleryInfo>> {

            return repository.getGalleryDataFromDb()
    }

    fun deleteGalleryDataFromDb(id: String)  {

        return repository.deleteGalleryDataFromDb(id)
    }

    fun deleteImageFromServer(body: RequestBody, apiCode: Int) {

        loadListener?.onStarted()
        Coroutines.mainIO {
            try {
                val response = repository.deleteImageFromServer(body)
                response?.let {
                    loadListener?.onSuccess(response, apiCode)
                    return@mainIO
                }
                loadListener?.onFailure(response.message!!, apiCode)

            } catch (e: ApiException) {
                loadListener?.onFailure(e.message!!, apiCode)
            } catch (e: NoInternetException) {
                loadListener?.onFailure(e.message!!, apiCode)
            }
        }


        //return repository.deleteImageFromServer(body)
    }

    fun insertImageInGallery(info : DocumentGallery):Long  {

        return repository.insertImageInGallery(info)
    }
    /**
     * Insert Outbound Data In Master
     */
    fun insertOutboundData(data: Outbound){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertOutboundData(data)
        }
    }
    fun getOutboundList(isSynced: Boolean): List<Outbound>{
        return repository.getOutboundList(isSynced)
    }

}