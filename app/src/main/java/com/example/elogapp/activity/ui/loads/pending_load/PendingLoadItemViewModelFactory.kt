package com.example.elogapp.activity.ui.loads.pending_load

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.LoadRepository

@Suppress("UNCHECKED_CAST")
class PendingLoadItemViewModelFactory(private val repository: LoadRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PendingLoadItemViewModel(repository) as T
    }
}