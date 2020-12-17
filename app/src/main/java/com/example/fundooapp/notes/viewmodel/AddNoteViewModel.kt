package com.example.fundooapp.notes.viewmodel

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

    private val _isNotesOperated = MutableLiveData<Boolean>()
    val isNotesOperated = _isNotesOperated as LiveData<Boolean>

    fun addNotes(noteDetails: Note) {
        notesService.addNotes(noteDetails) { note, exception ->
            note?.let {
                _addNoteStatus.value = ViewState.Success(note)
                _isNotesOperated.value = true
            }
        }
    }

    fun updateNotes(noteDetails: Note) {
        notesService.updateNotes(noteDetails) { status, exception ->
            if (status == true) {
                _updateNoteStatus.value = ViewState.Success(noteDetails)
                _isNotesOperated.value = true
            }
        }
    }

}