package com.example.elogapp.activity.ui.dispatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.HomeRepository

@Suppress("UNCHECKED_CAST")
class DispatchHomeViewModelFactory(
        private val repository: HomeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DispatchHomeViewModel(repository) as T
    }

}