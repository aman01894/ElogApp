package com.example.elogapp.activity.ui.dvir

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elogapp.database.DropdownMaster
import com.example.elogapp.repository.NewDvirRepository
import com.example.elogapp.util.ApiException
import com.example.elogapp.util.Coroutines
import com.example.elogapp.util.NoInternetException
import okhttp3.RequestBody


class NewDvirViewModel(private val repository: NewDvirRepository) : ViewModel() {

    var dvirListener: DvirListener? = null
    val backButtonLiveData = MutableLiveData<Void?>()


    fun getDefectList (type: String): LiveData<List<DropdownMaster>> {
        return repository.getDropdownList(type)
    }


    fun createNewDvirReport(event: RequestBody) {

        dvirListener?.onStarted()
        Coroutines.main {
            try {
                val authResponse = repository.saveNewDvirData(event)
                authResponse?.let {
                    dvirListener?.onSuccess(authResponse)
                    return@main
                }
                dvirListener?.onFailure(authResponse.message!!)

            } catch (e: ApiException) {
                dvirListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                dvirListener?.onFailure(e.message!!)
            }

        }

    }

    /**
     * On Back Button Pressed
     *
     */
    fun ressedBtn(view : View){

        backButtonLiveData.postValue(null)

    }

}