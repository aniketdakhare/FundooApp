package com.example.fundooapp.register.model

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)
