package com.example.fundooapp.addnotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.INotesService
import com.example.fundooapp.model.Note
import com.example.fundooapp.util.ViewState

class AddNoteViewModel(private val notesService: INotesService) : ViewModel() {

    private val _addNoteStatus = MutableLiveData<ViewState<Note>>()
    val addNoteStatus = _addNoteStatus as LiveData<ViewState<Note>>

    private val _updateNoteStatus = MutableLiveData<ViewState<Note>>()
    val updateNoteStatus = _updateNoteStatus as LiveData<ViewState<Note>>

    init {
        _addNoteStatus.value = ViewState.Loading(Note())
        _updateNoteStatus.value = ViewState.Loading(Note())
    }

    fun addNotes(noteDetails: Note) {
        notesService.addNotes(noteDetails) { note, _ ->
            note?.let {
                _addNoteStatus.value = ViewState.Success(note)
            }
        }
    }

    fun updateNotes(noteDetails: Note) {
        notesService.updateNotes(noteDetails) { status, _ ->
            if (status == true) {
                _updateNoteStatus.value = ViewState.Success(noteDetails)
            }
        }
    }
}