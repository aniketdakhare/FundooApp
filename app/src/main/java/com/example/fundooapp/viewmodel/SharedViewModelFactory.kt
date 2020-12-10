package com.example.fundooapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.INotesService
import com.example.fundooapp.model.IUserService
import com.example.fundooapp.model.UserService

class SharedViewModelFactory(private val userService: IUserService, private val notesService: INotesService) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SharedViewModel(userService, notesService) as T
    }
}
