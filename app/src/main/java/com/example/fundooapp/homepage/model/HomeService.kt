package com.example.fundooapp.homepage.model

import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth

class HomeService: IHomeService {
    override fun logoutUser() {
        LoginManager.getInstance().logOut()
        FirebaseAuth.getInstance().signOut()
    }
}