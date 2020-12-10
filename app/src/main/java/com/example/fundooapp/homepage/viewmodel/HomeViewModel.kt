package com.example.fundooapp.homepage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.INotesService
import com.example.fundooapp.model.Note

class HomeViewModel(private val notesService: INotesService) : ViewModel() {
}