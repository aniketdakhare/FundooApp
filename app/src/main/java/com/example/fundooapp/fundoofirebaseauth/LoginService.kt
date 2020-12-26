package com.example.fundooapp.fundoofirebaseauth

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.fundooapp.fundoofirebaseauth.util.HttpsHelper
import com.example.fundooapp.model.IUserService
import com.example.fundooapp.model.User
import com.example.fundooapp.util.AuthResponseDetails
import com.facebook.AccessToken

class LoginService: IUserService {
    private val httpsHelper = HttpsHelper()

    override fun authenticateUser(email: String, password: String, listener: (AuthResponseDetails) -> Unit) {
        Thread {
            val response = httpsHelper.loginUserWithEmailAndPassword(email, password)
            Handler(Looper.getMainLooper()).post( Runnable{
                listener(response)
            })
        }.start()
    }

    override fun registerUser(user: User, listener: (Boolean) -> Unit) {

    }

    override fun resetPassword(emailId: String, listener: (Boolean) -> Unit) {
    }

    override fun facebookLogin(token: AccessToken, listener: (User, Boolean) -> Unit) {
    }

    override fun logoutUser() {
    }

    override fun getUserDetails(localId: String, idToken: String, listener: (User) -> Unit) {
        Log.e("loginService", "getuserdetails: outside thread", )
        Thread {
            Log.e("loginService", "getuserdetails: Thread", )
            val status = httpsHelper.fetchDataFromFireStore(localId, idToken)
            Handler(Looper.getMainLooper()).post {
                Log.e("loginService", "getuserdetails: $status", )
                listener(status)
            }
        }.start()
    }

    override fun uploadImageToFirebase(uri: Uri) {
    }

    override fun getLoginStatus(listener: (Boolean) -> Unit) {
    }
}