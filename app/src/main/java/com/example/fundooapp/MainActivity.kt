package com.example.fundooapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), DelegateActivity {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginUser()
    }

    override fun registerUser()
    {
        val registerFragment = RegisterFragment()
        registerFragment.initiateActivity(this)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, registerFragment)
            commit()
        }
    }

    override fun loginUser() {
        val loginFragment = LoginFragment()
        loginFragment.initiateActivity(this)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, loginFragment)
            commit()
        }
    }

    override fun goToHomePage() {
        val homeFragment = HomeFragment()
        homeFragment.initiateActivity(this)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, homeFragment)
            commit()
        }
    }
}