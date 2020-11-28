package com.example.fundooapp.mainactivity.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.R
import com.example.fundooapp.homepage.view.HomeFragment
import com.example.fundooapp.login.view.LoginFragment
import com.example.fundooapp.mainactivity.viewmodel.SharedViewModel
import com.example.fundooapp.register.view.RegisterFragment

class MainActivity : AppCompatActivity() {
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        if (savedInstanceState == null) goToLoginUserPage()
        startOperation()
    }

    private fun startOperation(){
        sharedViewModel.goToHomePageStatus.observe(this, Observer {
            if (it == true) goToHomePage()
        })
        sharedViewModel.goToRegisterPageStatus.observe(this, Observer {
            if (it == true) goToRegisterUserPage()
        })
        sharedViewModel.goToLoginPageStatus.observe(this, Observer {
            if (it == true) goToLoginUserPage()
        })
    }

    private fun goToRegisterUserPage()
    {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, RegisterFragment())
            commit()
        }
    }

    private fun goToLoginUserPage() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, LoginFragment())
            commit()
        }
    }

    private fun goToHomePage() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, HomeFragment())
            commit()
        }
    }
}