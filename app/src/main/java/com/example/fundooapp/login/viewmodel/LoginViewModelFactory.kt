package com.example.fundooapp.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.login.model.IUserService
import com.example.fundooapp.login.view.LoginFragment

class LoginViewModelFactory(val loginFragment: LoginFragment, private val userService: IUserService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(userService) as T
    }
}
