package com.example.fundooapp.model

interface INotesService {
    fun fetchNotes(listener: (List<Note>, Exception?) -> Unit)
    fun updateLocalDB(listener: (Boolean?, Exception?) -> Unit)
    fun deleteNotes(note: Note, listener: (Boolean?, Exception?) -> Unit)
    fun addNotes(notes: Note, listener: (Note?, Exception?) -> Unit)
    fun updateNotes(notes: Note, listener: (Boolean?, Exception?) -> Unit)
}