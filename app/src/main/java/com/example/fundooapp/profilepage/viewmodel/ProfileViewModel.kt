package com.example.fundooapp.profilepage.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.IUserService

class ProfileViewModel(private val service: IUserService) : ViewModel() {
    private val _logoutStatus = MutableLiveData<Boolean>()
    val logoutStatus = _logoutStatus as LiveData<Boolean>

    fun logout() {
        _logoutStatus.value = true
        service.logoutUser()
    }

    fun uploadImageToFirebase(uri: Uri) {
        service.uploadImageToFirebase(uri)
    }
}