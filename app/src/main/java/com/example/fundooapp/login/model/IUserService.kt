package com.example.fundooapp.login.model

import com.facebook.AccessToken

interface IUserService {
    fun authenticateUser(email: String, password: String, listener: (Boolean) -> Unit)
    fun resetPassword(emailId: String, listener: (Boolean) -> Unit)
    fun facebookLogin(token: AccessToken, listener: (Boolean) -> Unit)
}