package com.example.fundooapp.model

interface DBContract {
    object UserTableContract {
        const val USERS = "Users"
        const val USER_ID = "User_ID"
        const val USER_NAME = "User_Name"
        const val USER_EMAIL = "Email_ID"
        const val USER_IMAGE_URL = "Profile_Image_Url"
    }

    object NotesTableContract {
        const val NOTE_ID = "Note_ID"
        const val NOTE_TITTLE = "Note_Title"
        const val NOTE_CONTENT = "Note_Content"
        const val NOTES = "Notes"
    }
}