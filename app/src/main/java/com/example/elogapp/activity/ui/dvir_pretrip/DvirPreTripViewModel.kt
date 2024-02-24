package com.example.elogapp.activity.ui.dvir_pretrip

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elogapp.repository.PreTripDvirRepository
import com.example.elogapp.util.ApiException
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.NoInternetException
import kotlinx.coroutines.launch


class DvirPreTripViewModel(private val repository: PreTripDvirRepository) : ViewModel() {


    var responseListener: DvirPreTripListener? = null
    val backButtonLiveData = MutableLiveData<Void?>()



    fun getPreDvirList (key: String) {

        responseListener?.onStarted()
        Log.d(AppConstants.TAG, "Pre Trip Dvir List",)
        viewModelScope.launch {

            try {
                val response = repository.getPreDvirList(key)
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