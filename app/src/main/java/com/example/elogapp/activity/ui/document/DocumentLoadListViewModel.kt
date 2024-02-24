package com.example.elogapp.activity.ui.document

import androidx.lifecycle.ViewModel
import com.example.elogapp.repository.LoadRepository
import com.example.elogapp.repository.responses.load.DocumentLoadListener
import com.example.elogapp.util.ApiException
import com.example.elogapp.util.Coroutines
import com.example.elogapp.util.NoInternetException

class DocumentLoadListViewModel(private val repository: LoadRepository) : ViewModel() {

    var documentLoadListener: DocumentLoadListener? = null

    /*fun getUserDetails (): LiveData<UserDetails> {

        return repository.getUserDetails()
    }*/

    fun getDocLoads(fromDates: String, toDates: String) {

        documentLoadListener?.onStarted()
        Coroutines.main {
            try {
                val authResponse = repository.getDocLoads(fromDates, toDates)
                authResponse?.let {
                    documentLoadListener?.onSuccess(authResponse)
                    return@main
                }
                documentLoadListener?.onFailure(authResponse.message!!)

            } catch (e: ApiException) {
                documentLoadListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                documentLoadListener?.onFailure(e.message!!)
            }
        }

    }


}