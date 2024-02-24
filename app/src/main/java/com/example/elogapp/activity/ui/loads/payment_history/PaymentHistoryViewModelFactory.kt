package com.example.elogapp.activity.ui.loads.payment_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.LoadRepository

@Suppress("UNCHECKED_CAST")
class PaymentHistoryViewModelFactory(private val repository: LoadRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentHistoryViewModel(repository) as T
    }
}