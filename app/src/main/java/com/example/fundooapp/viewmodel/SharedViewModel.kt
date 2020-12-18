package com.example.fundooapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.INotesService
import com.example.fundooapp.model.IUserService
import com.example.fundooapp.model.Note
import com.example.fundooapp.model.User
import com.example.fundooapp.util.NotesOperation
import com.example.fundooapp.util.ViewState
import com.example.fundooapp.util.ViewType

class SharedViewModel(
    private val userService: IUserService,
    private val notesService: INotesService
) : ViewModel() {

    init {
        fetchUserDetails()
    }

    private val _showAddNoteFab = MutableLiveData<Boolean>()
    val showAddNoteFab = _showAddNoteFab as LiveData<Boolean>

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

    private val _writeNote = MutableLiveData<Pair<Note, NotesOperation>>()
    val writeNote = _writeNote as LiveData<Pair<Note, NotesOperation>>

    private val _queryText = MutableLiveData<String>()
    val queryText = _queryText as LiveData<String>

    private val _addNoteStatus = MutableLiveData<ViewState<Note>>()
    val addNoteStatus = _addNoteStatus as LiveData<ViewState<Note>>

    private val _updateNoteStatus = MutableLiveData<ViewState<Note>>()
    val updateNoteStatus = _updateNoteStatus as LiveData<ViewState<Note>>

    private val _notesDisplayType = MutableLiveData<ViewType>()
    val notesDisplayType = _notesDisplayType as LiveData<ViewType>

    fun setNotesDisplayType(viewType: ViewType) {
        _notesDisplayType.value = viewType
    }

    fun setQueryText(string: String?) {
        _queryText.value = string
    }

    fun setNoteToWrite(note: Pair<Note, NotesOperation>?) {
        _writeNote.value = note
    }

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

    fun setAddNoteStatus(state: ViewState<Note>?) {
        _addNoteStatus.value = state
    }

    fun setUpdateNoteStatus(state: ViewState<Note>?) {
        _updateNoteStatus.value = state
    }

    fun setAddNoteFab(status: Boolean) {
        _showAddNoteFab.value = status
    }

}