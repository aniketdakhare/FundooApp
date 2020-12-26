package com.example.fundooapp.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.fundoofirebaseauth.LoginService
import com.example.fundooapp.model.IUserService
import com.example.fundooapp.model.UserService

class LoginViewModelFactory(private val userService: UserService, private val loginService: LoginService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(userService, loginService) as T
    }
}
