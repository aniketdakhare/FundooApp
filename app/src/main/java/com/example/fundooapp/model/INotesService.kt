package com.example.fundooapp.model

interface INotesService {
    fun notesDbOperation(notes: Note, listener: (Boolean) -> Unit)
    fun fetchNotes(listener: (List<Note>) -> Unit)
}