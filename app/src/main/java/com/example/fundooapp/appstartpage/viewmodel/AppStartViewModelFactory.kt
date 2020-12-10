package com.example.fundooapp.appstartpage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.INotesService
import com.example.fundooapp.model.IUserService

class AppStartViewModelFactory(private val service: IUserService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppStartViewModel(service) as T
    }
}
