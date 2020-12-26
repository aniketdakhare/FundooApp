package com.example.fundooapp.model

import android.net.Uri
import com.example.fundooapp.util.AuthResponseDetails
import com.facebook.AccessToken

interface IUserService {
    fun authenticateUser(email: String, password: String, listener: (AuthResponseDetails) -> Unit)
    fun registerUser(user: User, listener: (Boolean) -> Unit)
    fun resetPassword(emailId: String, listener: (Boolean) -> Unit)
    fun facebookLogin(token: AccessToken, listener: (User, Boolean) -> Unit)
    fun logoutUser()
    fun getUserDetails(localId: String, idToken: String, listener: (User) -> Unit)
    fun uploadImageToFirebase(uri: Uri)
    fun getLoginStatus(listener: (Boolean) -> Unit)
}