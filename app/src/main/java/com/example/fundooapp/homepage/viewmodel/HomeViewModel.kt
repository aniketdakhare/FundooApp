package com.example.fundooapp.homepage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.homepage.model.IHomeService

class HomeViewModel(private val homeService: IHomeService) : ViewModel() {
    private val _logoutStatus = MutableLiveData<Boolean>()
    val logoutStatus = _logoutStatus as LiveData<Boolean>

    fun logout() {
        _logoutStatus.value = true
        homeService.logoutUser()
    }
}