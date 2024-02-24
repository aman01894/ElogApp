package com.example.elogapp.activity.ui.dvir

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.NewDvirRepository

@Suppress("UNCHECKED_CAST")
class NewDvirViewModelFactory(private val repository: NewDvirRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewDvirViewModel(repository) as T
    }

}