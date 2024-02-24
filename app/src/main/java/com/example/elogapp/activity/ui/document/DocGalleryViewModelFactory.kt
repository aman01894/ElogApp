package com.example.elogapp.activity.ui.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.repository.DocGalleryRepository

@Suppress("UNCHECKED_CAST")
class DocGalleryViewModelFactory(private val repository: DocGalleryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DocGalleryViewModel(repository) as T
    }
}