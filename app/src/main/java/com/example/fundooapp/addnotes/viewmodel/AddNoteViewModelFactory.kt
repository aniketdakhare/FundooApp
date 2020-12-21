package com.example.fundooapp.addnotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.INotesService

class AddNoteViewModelFactory(private val service: INotesService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddNoteViewModel(service) as T
    }
}
