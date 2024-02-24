package com.example.elogapp.activity.ui.shippingdocs

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elogapp.database.ShippingDocs
import com.example.elogapp.database.UserDetails
import com.example.elogapp.repository.ShippingDocsRepository
import com.example.elogapp.repository.responses.shippingdocs.ShippingDocsData
import com.example.elogapp.util.ApiException
import com.example.elogapp.util.AppUtils
import com.example.elogapp.util.NoInternetException
import com.example.elogapp.util.apilistner.ApiResponseListener
import kotlinx.coroutines.launch


class ShippingDocsViewModel(private val repository: ShippingDocsRepository) : ViewModel() {


    var responseListener: ApiResponseListener? = null
    val backButtonLiveData = MutableLiveData<Void?>()



    fun getUserDetails (): LiveData<UserDetails> {
        return repository.getUserDetails()
    }

    fun getShippingDocsListFromDb() : LiveData<List<ShippingDocs>> {
        return repository.getShippingDocsList()
    }

    fun insertShippingDocInDb(docInfo : ShippingDocs) : Long {
        return repository.insertShippingDoc(docInfo)
    }

    fun getShippingDocListFromServer() {

        responseListener?.onStarted()
        viewModelScope.launch {

            try {
                val response = repository.getShippingDocListFromServer()
                AppUtils.logger("Response: $response")
                responseListener?.onSuccess(response)
            } catch (e: NoInternetException) {
                e.printStackTrace()
                responseListener?.onFailure(e.message!!)
            } catch (e: ApiException) {
                e.printStackTrace()
                responseListener?.onFailure(e.message!!)
            }
        }
    }

    /**
     * On Back Button Pressed
     *
     */
    fun onBackPressedBtn(view : View){

        backButtonLiveData.postValue(null)

    }



}