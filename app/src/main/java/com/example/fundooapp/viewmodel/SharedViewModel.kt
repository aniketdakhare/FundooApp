package com.example.fundooapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.*
import com.example.fundooapp.util.ViewType

class SharedViewModel(private val userService: IUserService, private val notesService: INotesService) : ViewModel() {

    init {
        fetchUserDetails()
        fetchProfileImageUri()
        getAllNotes()
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

    private fun fetchProfileImageUri() {
        userService.getProfileImage {
            _imageUri.value = it
        }
    }

    fun setUserDetails(user: User) {
        _userDetails.value = user
    }

    private val _notesDisplayType = MutableLiveData<ViewType>()
    val notesDisplayType = _notesDisplayType as LiveData<ViewType>

    private val _notesOperation = MutableLiveData<Note>()
    val notesOperation = _notesOperation as LiveData<Note>

    private val _isNotesOperated = MutableLiveData<Boolean>()
    val isNotesOperated = _isNotesOperated as LiveData<Boolean>

    private val _notes = MutableLiveData<List<Note>>()
    val notes = _notes as LiveData<List<Note>>

    fun notesOperation(notes: Note) {
        notesService.notesDbOperation(notes){
            _isNotesOperated.value = it
        }
    }

    fun getAllNotes(){
        notesService.fetchNotesFromFireBase{
            _notes.value = it
        }
    }

    fun setNotesOperation(note: Note) {
        _notesOperation.value = note
    }

    fun setNotesDisplayType(viewType: ViewType) {
        _notesDisplayType.value = viewType
    }
}