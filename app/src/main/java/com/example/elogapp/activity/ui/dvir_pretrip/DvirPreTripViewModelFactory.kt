package com.example.elogapp.activity.ui.dvir_pretrip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.PreTripDvirRepository

@Suppress("UNCHECKED_CAST")
class DvirPreTripViewModelFactory(private val repository: PreTripDvirRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DvirPreTripViewModel(repository) as T
    }

}