package com.example.fundooapp.model

import android.net.Uri

data class User(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val fullName: String = "",
    val imageUrl: String = "",
    val imageUri: Uri? = null
)
