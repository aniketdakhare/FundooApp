package com.example.fundooapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.INotesService
import com.example.fundooapp.model.IUserService
import com.example.fundooapp.model.Note
import com.example.fundooapp.model.User
import com.example.fundooapp.notesdisplay.view.NotesViewAdapter
import com.example.fundooapp.util.NotesOperation
import com.example.fundooapp.util.ViewType

class SharedViewModel(
    private val userService: IUserService,
    private val notesService: INotesService
) : ViewModel() {

    init {
        fetchUserDetails()
    }

    private val _goToRegisterPageStatus = MutableLiveData<Boolean>()
    val goToRegisterPageStatus = _goToRegisterPageStatus as LiveData<Boolean>

    private val _goToLoginPageStatus = MutableLiveData<Boolean>()
    val goToLoginPageStatus = _goToLoginPageStatus as LiveData<Boolean>

    private val _goToHomePageStatus = MutableLiveData<Boolean>()
    val goToHomePageStatus = _goToHomePageStatus as LiveData<Boolean>

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri = _imageUri as LiveData<Uri>

    private val _userDetails = MutableLiveData<User>()
    val userDetails = _userDetails as LiveData<User>

    fun setGoToRegisterPageStatus(status: Boolean) {
        _goToRegisterPageStatus.value = status
    }

    fun setGoToLoginPageStatus(status: Boolean) {
        _goToLoginPageStatus.value = status
    }

    fun setGoToHomePageStatus(status: Boolean) {
        _goToHomePageStatus.value = status
    }

    fun setImageUri(imageUri: Uri) {
        _imageUri.value = imageUri
    }

    fun fetchUserDetails() {
        userService.getUserDetails {
            _userDetails.value = it
        }
    }

    fun setUserDetails(user: User) {
        _userDetails.value = user
    }

    private val _notesDisplayType = MutableLiveData<ViewType>()
    val notesDisplayType = _notesDisplayType as LiveData<ViewType>

    private val _writeNote = MutableLiveData<Pair<Note, NotesOperation>>()
    val writeNote = _writeNote as LiveData<Pair<Note, NotesOperation>>

    private val _isNotesOperated = MutableLiveData<Boolean>()
    val isNotesOperated = _isNotesOperated as LiveData<Boolean>

    private val _notesUpdateStatus = MutableLiveData<Boolean>()
    val notesUpdateStatus = _notesUpdateStatus as LiveData<Boolean>

    private val _isNoteDeleted = MutableLiveData<Boolean>()
    val isNoteDeleted = _isNoteDeleted as LiveData<Boolean>

    private val _notes = MutableLiveData<List<Note>>()
    val notes = _notes as LiveData<List<Note>>

    fun addNotes(notes: Note) {
        notesService.addNotes(notes) {
            _isNotesOperated.value = it
        }
    }

    fun deleteNotes(notes: Note) {
        notesService.deleteNotes(notes) {
            _isNoteDeleted.value = it
        }
    }

    fun updateNotes(notes: Note) {
        notesService.updateNotes(notes) {
            _isNotesOperated.value = it
        }
    }

    fun getAllNotes() {
        notesService.fetchNotes {
            _notes.value = it
        }
    }

    fun setNoteToWrite(note: Pair<Note, NotesOperation>) {
        _writeNote.value = note
    }

    fun setNotesDisplayType(viewType: ViewType) {
        _notesDisplayType.value = viewType
    }

    fun updateNotes() {
        notesService.updateLocalDB(){
            _notesUpdateStatus.value = true
        }
    }

    private val _notesAdapter = MutableLiveData<NotesViewAdapter>()
    val notesAdapter = _notesAdapter as LiveData<NotesViewAdapter>

    fun setNotesAdapter(adapter: NotesViewAdapter) {
        _notesAdapter.value = adapter
    }
}