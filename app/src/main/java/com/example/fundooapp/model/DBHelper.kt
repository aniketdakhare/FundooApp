package com.example.fundooapp.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fundooapp.model.DBContract.NotesTableContract.NOTES
import com.example.fundooapp.model.DBContract.NotesTableContract.NOTE_CONTENT
import com.example.fundooapp.model.DBContract.NotesTableContract.NOTE_ID
import com.example.fundooapp.model.DBContract.NotesTableContract.NOTE_TITTLE
import com.example.fundooapp.model.DBContract.UserTableContract.USERS
import com.example.fundooapp.model.DBContract.UserTableContract.USER_EMAIL
import com.example.fundooapp.model.DBContract.UserTableContract.USER_ID
import com.example.fundooapp.model.DBContract.UserTableContract.USER_IMAGE_URI
import com.example.fundooapp.model.DBContract.UserTableContract.USER_IMAGE_URL
import com.example.fundooapp.model.DBContract.UserTableContract.USER_NAME

class DBHelper(context: Context) : SQLiteOpenHelper(context, "User.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createUser =
            "CREATE TABLE $USERS($USER_ID TEXT PRIMARY KEY, $USER_NAME TEXT, $USER_EMAIL TEXT, $USER_IMAGE_URL TEXT, $USER_IMAGE_URI TEXT)"
        val createNotes =
            "CREATE TABLE $NOTES($USER_ID TEXT , $NOTE_ID INT PRIMARY KEY AUTOINCREMENT, $NOTE_TITTLE TEXT, $NOTE_CONTENT TEXT)"

        db?.execSQL(createUser)
        db?.execSQL(createNotes)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun addUser(user: User) {
        val db = this.writableDatabase
        val userDetails = ContentValues()

        userDetails.put(USER_ID, user.userId)
        userDetails.put(USER_NAME, user.fullName)
        userDetails.put(USER_EMAIL, user.email)
        userDetails.put(USER_IMAGE_URL, user.imageUrl)
        userDetails.put(USER_IMAGE_URI, user.imageUri.toString())

        db.insert(USERS, null, userDetails)
    }

    fun addNote(note: Note) {
        val db = this.writableDatabase
        val noteDetails = ContentValues()

        noteDetails.put(USER_ID, note.userId)
        noteDetails.put(NOTE_ID, note.noteId)
        noteDetails.put(NOTE_TITTLE, note.tittle)
        noteDetails.put(NOTE_CONTENT, note.content)

        db.insert(NOTES, null, noteDetails)
        db.close()
    }

    fun updateNote(note: Note) {
        val db = this.writableDatabase
        val query = "UPDATE $NOTES SET $NOTE_TITTLE = ${note.tittle}, $NOTE_CONTENT = ${note.content} WHERE $NOTE_ID = \"${note.dbNoteId}\""
        val cursor = db.rawQuery(query, null)
        cursor.close()
        db.close()
    }

    fun deleteNote(note: Note) {
        val db = this.writableDatabase
        val query = "DELETE FROM $NOTES WHERE $NOTE_TITTLE = \"${note.tittle}\""
        val cursor = db.rawQuery(query, null)
        cursor.close()
        db.close()
    }

    fun fetchNotes(listener: (List<Note>) -> Unit, uid: String) {
        val notes: MutableList<Note> = mutableListOf()

        val query = "SELECT * FROM $NOTES WHERE $USER_ID = \"$uid\""
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                notes.add(Note(
                    userId = cursor.getString(0),
                    dbNoteId = cursor.getInt(1),
                    tittle = cursor.getString(2),
                    content = cursor.getString(3)
                ))
            }while (cursor.moveToNext())
            listener(notes)
        }

        cursor.close()
        db.close()
    }
}