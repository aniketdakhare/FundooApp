package com.example.fundooapp.homepage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.INotesService
import com.example.fundooapp.model.Note
import com.example.fundooapp.util.NotesDisplayType
import com.example.fundooapp.util.NotesDisplayType.ALL_NOTES
import com.example.fundooapp.util.NotesDisplayType.REMINDER_NOTES
import com.example.fundooapp.util.ViewState

class HomeViewModel(private val notesService: INotesService) : ViewModel() {

    private var _notes = listOf<Note>()
    private var _reminderNotes = mutableListOf<Note>()

    private val _notesViewState = MutableLiveData<ViewState<List<Note>>>()
    val notesViewState = _notesViewState as LiveData<ViewState<List<Note>>>

    init {
        _notesViewState.value = ViewState.Loading(emptyList())
        updateLocalDb()
    }

    fun deleteNotes(noteDetails: Note) {
        notesService.deleteNotes(noteDetails) { status, _ ->
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

    private fun updateLocalDb() {
        notesService.updateLocalDB() { isUpdated, _ ->
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

    fun displayNotesAsPerType(notesDisplayType: NotesDisplayType?) {
        when (notesDisplayType) {
            REMINDER_NOTES -> {
                _reminderNotes.clear()
                for (note in _notes) {
                    if (note.reminderTime != 0L)
                        _reminderNotes.add(note)
                }
                _notesViewState.value = ViewState.Success(_reminderNotes)
            }
            ALL_NOTES -> {
                _notesViewState.value = ViewState.Success(_notes)
            }
        }
    }
}