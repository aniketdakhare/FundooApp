package com.example.fundooapp.model

import com.example.fundooapp.util.NotesOperation
import com.example.fundooapp.util.NotesOperation.ADD

class Note(
    val tittle: String = "",
    val content: String = "",
    val noteId: String = "",
    val operation: NotesOperation = ADD,
    var userId: String = "",
    var dbNoteId: Int = 0
)
