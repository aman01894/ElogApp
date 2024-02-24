package com.example.elogapp.activity.ui.company_info


import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elogapp.repository.CompanyInfoRepository
import com.example.elogapp.util.ApiException
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.NoInternetException
import kotlinx.coroutines.launch


class CompanyViewModel(private val repository: CompanyInfoRepository) : ViewModel() {


    var responseListener: CompanyInfoListener? = null
    val backButtonLiveData = MutableLiveData<Void?>()


    fun getCompanyInfo (id: Int) {

        responseListener?.onStarted()
        Log.d(AppConstants.TAG, "Company Info")
        viewModelScope.launch {

            try {
                val response = repository.getCustomerInfo(id)
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