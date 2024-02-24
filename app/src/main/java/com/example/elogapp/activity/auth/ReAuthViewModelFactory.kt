package com.example.elogapp.activity.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.UserRepository

@Suppress("UNCHECKED_CAST")
class ReAuthViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReAuthViewModel(repository) as T
    }

}