package com.example.elogapp.activity.ui.unidentifiedevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.ExceptionRepository
import com.example.elogapp.repository.UnidentifiedDataRepository

@Suppress("UNCHECKED_CAST")
class UnidentifiedEventViewModelFactory(private val repository: UnidentifiedDataRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UnidentifiedEventViewModel(repository) as T
    }
}