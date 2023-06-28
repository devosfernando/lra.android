package com.example.fcmpushnotification.ui.webSite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WebSiteViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Recurso no disponible"
    }
    val text: LiveData<String> = _text
}