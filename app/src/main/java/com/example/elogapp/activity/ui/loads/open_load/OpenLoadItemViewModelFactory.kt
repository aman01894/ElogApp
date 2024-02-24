package com.example.elogapp.activity.ui.loads.open_load

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.LoadRepository

@Suppress("UNCHECKED_CAST")
class OpenLoadItemViewModelFactory(private val repository: LoadRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OpenLoadItemViewModel(repository) as T
    }
}