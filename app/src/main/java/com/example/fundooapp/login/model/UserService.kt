package com.example.fundooapp.login.model

import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

class UserService : IUserService {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun authenticateUser(email: String, password: String, listener: (Boolean) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
           listener(it.isSuccessful)
         }
    }

    override fun resetPassword(emailId: String, listener: (Boolean) -> Unit) {
        firebaseAuth.sendPasswordResetEmail(emailId).addOnCompleteListener {
            listener( it.isSuccessful)
        }
    }

    override fun facebookLogin(token: AccessToken, listener: (Boolean) -> Unit) {
        val credentials = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credentials).addOnCompleteListener {
            listener(it.isSuccessful)
        }
    }
}