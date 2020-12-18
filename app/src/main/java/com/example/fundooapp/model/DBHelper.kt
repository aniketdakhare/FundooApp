package com.example.fundooapp.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.fundooapp.model.DBContract.NotesTableContract.NOTES
import com.example.fundooapp.model.DBContract.NotesTableContract.NOTE_CONTENT
import com.example.fundooapp.model.DBContract.NotesTableContract.NOTE_ID
import com.example.fundooapp.model.DBContract.NotesTableContract.NOTE_TITTLE
import com.example.fundooapp.model.DBContract.UserTableContract.USER_ID

class DBHelper(context: Context) : SQLiteOpenHelper(context, "FundooNotes.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createNotes =
            "CREATE TABLE $NOTES($USER_ID TEXT , $NOTE_ID TEXT PRIMARY KEY, $NOTE_TITTLE TEXT, $NOTE_CONTENT TEXT)"

        db?.execSQL(createNotes)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun addNote(note: Note, listener: (Boolean) -> Unit) {
        val db = this.writableDatabase
        val noteDetails = ContentValues()

        noteDetails.put(USER_ID, note.userId)
        noteDetails.put(NOTE_ID, note.noteId)
        noteDetails.put(NOTE_TITTLE, note.tittle)
        noteDetails.put(NOTE_CONTENT, note.content)

        val insertCheck = db.insert(NOTES, null, noteDetails)
        if (insertCheck.toInt() == -1) {
            listener(false)
        } else listener(true)
        Log.e("DBHelper", "addNote: inserted")
        db.close()
    }

    fun updateNote(note: Note, listener: (Boolean) -> Unit) {
        val db = this.writableDatabase
        val noteDetails = ContentValues()

        noteDetails.put(NOTE_TITTLE, note.tittle)
        noteDetails.put(NOTE_CONTENT, note.content)

        val updateCheck = db.update(
            NOTES,
            noteDetails,
            "$NOTE_ID = \"${note.noteId}\" AND $USER_ID = \"${note.userId}\"",
            null
        )
        if (updateCheck > 0) {
            listener(true)
            Log.e("DBHelper", "Update: true")
        } else {
            listener(false)
            Log.e("DBHelper", "Update: False")
        }

        db.close()
    }

    fun deleteNote(note: Note, listener: (Boolean) -> Unit) {
        val db = this.writableDatabase

        val deleteCheck = db.delete(NOTES, "$NOTE_ID = \"${note.noteId}\"", null)
        if (deleteCheck > 0) {
            listener(true)
            Log.e("DBHelper", "Delete: true")
        } else {
            listener(false)
            Log.e("DBHelper", "Delete: False")
        }
        db.close()
    }

    fun fetchNotes(uid: String, listener: (MutableList<Note>) -> Unit) {
        val notes: MutableList<Note> = mutableListOf()

        val query = "SELECT * FROM $NOTES WHERE $USER_ID = \"$uid\""
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                Log.e("DBHelper", "fetchNotes: Inside While loop")
                notes.add(
                    Note(
                        userId = cursor.getString(0),
                        noteId = cursor.getString(1),
                        tittle = cursor.getString(2),
                        content = cursor.getString(3)
                    )
                )
            } while (cursor.moveToNext())
            listener(notes)
        } else {
            Log.e("DBHelper", "fetchNotes: else part")

            listener(notes)
        }

        Log.e("DBHelper", "fetchNotes: ")
        cursor.close()
        db.close()
    }

    fun clearNotes() {
        val db = this.writableDatabase
        val query = "DELETE FROM $NOTES"

        db.execSQL(query)
        db.close()
    }
}