package com.example.elogapp.activity.ui.dispatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elogapp.database.UserDetails
import com.example.elogapp.repository.HomeRepository
import com.example.elogapp.util.pref.UserPreference
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class DispatchHomeViewModel(private var repository: HomeRepository) : ViewModel() {

    var userId: String? = null
    var clientId: String? = null

    fun getUserDetail() = repository.getUser()


    fun saveDutyIdPref(context: UserPreference, dutyId: String) {

        viewModelScope.async {

            context.saveDutyId(dutyId)
        }
    }


    fun saveDutyStartTimePref(context: UserPreference, dutyTime: String) {

        viewModelScope.launch {

            context.saveStartDutyTime(dutyTime)
        }
    }


    fun getUserDetails(): LiveData<UserDetails> {

        return repository.getUserDetails()
    }


}