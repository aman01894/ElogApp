package com.example.elogapp.activity.ui.shippingdocs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.ShippingDocsRepository

@Suppress("UNCHECKED_CAST")
class ShippingDocsViewModelFactory(private val repository: ShippingDocsRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShippingDocsViewModel(repository) as T
    }

}