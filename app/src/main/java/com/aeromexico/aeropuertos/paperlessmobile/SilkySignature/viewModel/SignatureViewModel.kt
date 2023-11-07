package com.aeromexico.aeropuertos.paperlessmobile.SilkySignature.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignatureViewModel : ViewModel() {
    val base64 = MutableLiveData<String>()

    fun setBase64String(base64: String){
        this.base64.value = base64
    }
    fun getBase64String():LiveData<String>{
        return base64
    }
}