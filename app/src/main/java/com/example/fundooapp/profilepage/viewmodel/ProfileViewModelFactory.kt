package com.example.fundooapp.profilepage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.IUserService

class ProfileViewModelFactory(private val profilePageService: IUserService) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(profilePageService) as T
    }
}
