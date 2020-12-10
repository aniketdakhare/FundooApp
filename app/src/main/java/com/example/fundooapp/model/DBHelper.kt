package com.example.fundooapp.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "User.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createUser =
            "CREATE TABLE $USERS($USER_ID TEXT PRIMARY KEY, $USER_NAME TEXT, $USER_EMAIL TEXT, $USER_IMAGE_URL TEXT, $USER_IMAGE_URI TEXT)"
        val createNotes =
            "CREATE TABLE $NOTES($USER_ID TEXT,$NOTE_ID TEXT,$NOTE_TITTLE TEXT,$NOTE_CONTENT TEXT,FOREIGN KEY ($USER_ID) REFERENCES Users($USER_ID))"

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
    }

    companion object {
        private const val USER_ID = "User_ID"
        private const val USER_NAME = "User_Name"
        private const val USER_EMAIL = "Email_ID"
        private const val USER_IMAGE_URL = "Profile_Image_Url"
        private const val USER_IMAGE_URI = "Profile_Image_Uri"
        private const val NOTE_ID = "Note_ID"
        private const val NOTE_TITTLE = "Note_Title"
        private const val NOTE_CONTENT = "Note_Content"
        private const val USERS = "Users"
        private const val NOTES = "Notes"
    }
}