package com.example.elogapp.activity.ui.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elogapp.repository.UserRepository
import com.example.elogapp.repository.responses.load.LoadListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogoutViewModel(private val repository: UserRepository) : ViewModel() {

    var loadListener: LoadListener? = null

    fun logoutUser(id: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(id)
        }
    }

    fun getUserDetail() = repository.getUser()


}