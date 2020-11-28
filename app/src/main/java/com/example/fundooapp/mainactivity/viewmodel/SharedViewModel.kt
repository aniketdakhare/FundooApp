package com.example.fundooapp.mainactivity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
   private val _goToRegisterPageStatus = MutableLiveData<Boolean>()
   val goToRegisterPageStatus = _goToRegisterPageStatus as LiveData<Boolean>

    private val _goToLoginPageStatus = MutableLiveData<Boolean>()
    val goToLoginPageStatus = _goToLoginPageStatus as LiveData<Boolean>

    private val _goToHomePageStatus = MutableLiveData<Boolean>()
    val goToHomePageStatus = _goToHomePageStatus as LiveData<Boolean>

    fun setGoToRegisterPageStatus(status: Boolean)
    {
        _goToRegisterPageStatus.value = status
    }

    fun setGoToLoginPageStatus(status: Boolean)
    {
        _goToLoginPageStatus.value = status
    }
    fun setGoToHomePageStatus(status: Boolean)
    {
        _goToHomePageStatus.value = status
    }
}