package com.example.fundooapp.model

interface INotesService {
    fun notesDbOperation(notes: Note, listener: (Boolean) -> Unit)
    fun fetchNotesFromFireBase(listener: (List<Note>) -> Unit)
}