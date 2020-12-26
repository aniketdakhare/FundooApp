package com.example.fundooapp.util

data class AuthResponseDetails(
    val registeredStatus: Boolean,
    var idToken: String = "",
    var localId: String = ""
)
