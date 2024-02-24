package com.example.elogapp.activity.ui.exception

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.ExceptionRepository

@Suppress("UNCHECKED_CAST")
class ExceptionsViewModelFactory(private val repository: ExceptionRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExceptionViewModel(repository) as T
    }
}