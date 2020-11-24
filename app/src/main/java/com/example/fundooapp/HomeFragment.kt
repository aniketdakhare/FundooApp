package com.example.fundooapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var delegateActivity: DelegateActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        LoginManager.getInstance().logOut()
        FirebaseAuth.getInstance().signOut()
        delegateActivity.loginUser()
    }

    fun initiateActivity(mainActivity: DelegateActivity)
    {
        this.delegateActivity = mainActivity
    }
}