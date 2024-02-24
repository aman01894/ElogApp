package com.example.elogapp.activity.ui.loads.open_payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.LoadRepository

@Suppress("UNCHECKED_CAST")
class OpenPaymentViewModelFactory(private val repository: LoadRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OpenPaymentViewModel(repository) as T
    }
}