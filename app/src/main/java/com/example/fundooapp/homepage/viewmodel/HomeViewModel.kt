package com.example.fundooapp.homepage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.INotesService
import com.example.fundooapp.model.Note
import com.example.fundooapp.util.NotesOperation
import com.example.fundooapp.util.ViewState

class HomeViewModel(private val notesService: INotesService) : ViewModel() {

    private var _notes = listOf<Note>()

    private val _notesViewState = MutableLiveData<ViewState<List<Note>>>()
    val notesViewState = _notesViewState as LiveData<ViewState<List<Note>>>

    private val _writeNote = MutableLiveData<Pair<Note, NotesOperation>>()
    val writeNote = _writeNote as LiveData<Pair<Note, NotesOperation>>

    init {
        _notesViewState.value = ViewState.Loading(emptyList())
        updateLocalDb()
    }

    fun deleteNotes(noteDetails: Note) {
        notesService.deleteNotes(noteDetails) { status: Boolean?, exception: Exception? ->
            if (status == true) {
                val filter = _notes.filter {
                    it.noteId == noteDetails.noteId
                }
                _notes = _notes - filter
                _notesViewState.value = ViewState.Success(_notes)
            }
        }
    }

    fun addNotes(note: Note) {
        note.let {
            _notes = _notes + note
            _notesViewState.value = ViewState.Success(_notes)
        }
    }

    fun updateNotes(note: Note) {
        val filter = _notes.filter {
            it.noteId == note.noteId
        }

        if (filter.isNotEmpty()) {
            val list = _notes - filter[0]
            _notes = list + note
            _notesViewState.value = ViewState.Success(_notes)
        }
    }

    fun setNoteToWrite(note: Pair<Note, NotesOperation>?) {
        _writeNote.value = note
    }

    private fun updateLocalDb() {
        notesService.updateLocalDB() { isUpdated, exception ->
            if (isUpdated == true) getAllNotes()
        }
    }

    private fun getAllNotes() {
        notesService.fetchNotes { list: List<Note>, exception: Exception? ->
            _notes = list
            _notesViewState.value = ViewState.Success(list)
            exception?.let {
                _notesViewState.value =
                    ViewState.Failure(it.localizedMessage ?: "Something went wrong")
            }
        }
    }
}