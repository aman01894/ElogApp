package com.example.elogapp.activity.ui.loads.closed_load

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.LoadRepository

@Suppress("UNCHECKED_CAST")
class ClosedLoadsListViewModelFactory(private val repository: LoadRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClosedLoadsListViewModel(repository) as T
    }
}