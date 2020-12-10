package com.example.fundooapp.homepage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.INotesService


class HomeViewModelFactory(private val notesService: INotesService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(notesService) as T
    }
}
