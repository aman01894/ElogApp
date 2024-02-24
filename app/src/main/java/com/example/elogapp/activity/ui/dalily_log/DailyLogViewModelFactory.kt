package com.example.elogapp.activity.ui.dalily_log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.DailyLogRepository

@Suppress("UNCHECKED_CAST")
class DailyLogViewModelFactory(private val repository: DailyLogRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DailyLogViewModel(repository) as T
    }
}