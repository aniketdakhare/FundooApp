package com.example.fundooapp.appstartpage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.IUserService
import com.example.fundooapp.model.User

class AppStartViewModel(private val service: IUserService): ViewModel() {

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn = _isUserLoggedIn as LiveData<Boolean>

    fun checkUserExistence() {
        service.getLoginStatus{
            _isUserLoggedIn.value = it
        }
    }
}