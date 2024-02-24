package com.example.elogapp.activity.ui.company_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.CompanyInfoRepository

@Suppress("UNCHECKED_CAST")
class CompanyInfoViewModelFactory(private val repository: CompanyInfoRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CompanyViewModel(repository) as T
    }
}