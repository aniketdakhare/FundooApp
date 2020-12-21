package com.example.fundooapp.remindersettings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.model.INotesService

class SetReminderViewModelFactory(private val service: INotesService) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SetReminderViewModel(service) as T
    }
}
