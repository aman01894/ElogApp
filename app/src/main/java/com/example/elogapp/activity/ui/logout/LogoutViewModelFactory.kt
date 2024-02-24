package com.example.elogapp.activity.ui.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.UserRepository

@Suppress("UNCHECKED_CAST")
class LogoutViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LogoutViewModel(repository) as T
    }
}