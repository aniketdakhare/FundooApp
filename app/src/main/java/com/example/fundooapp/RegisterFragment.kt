package com.example.fundooapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var rFirstName: EditText
    private lateinit var rLastName: EditText
    private lateinit var rEmail: EditText
    private lateinit var rPassword: EditText
    private lateinit var rConfirmPassword: EditText
    private lateinit var signUp: Button
    private lateinit var registeredUser: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var delegateActivity: DelegateActivity
    private lateinit var fireStore: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRegister(view)
        signUp.setOnClickListener {
            checkRegistrationDetails()
        }
        goToLoginPage()
    }

    private fun goToLoginPage() {
        registeredUser.setOnClickListener {
            delegateActivity.loginUser()
        }
    }

    fun initiateActivity(mainActivity: DelegateActivity)
    {
        this.delegateActivity = mainActivity
    }

    private fun initRegister(view: View) {
        rFirstName = view.findViewById(R.id.firstName)
        rLastName = view.findViewById(R.id.lastName)
        rEmail = view.findViewById(R.id.registerEmail)
        rPassword = view.findViewById(R.id.registerPassword)
        rConfirmPassword = view.findViewById(R.id.registerConfirmPassword)
        signUp = view.findViewById(R.id.signUpButton)
        registeredUser = view.findViewById(R.id.registerTextView)
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        progressBar = view.findViewById(R.id.registerProgressBar)
    }

    private fun checkRegistrationDetails() {
        val email = rEmail.text.toString().trim()
        val password = rPassword.text.toString().trim()
        val confirmPassword = rConfirmPassword.text.toString().trim()

        when {
            email.isEmpty() -> {
                rEmail.error = "Please Enter Email Id"
                rEmail.requestFocus()
            }
            password.isEmpty() -> {
                rPassword.error = "Please Enter Password"
                rPassword.requestFocus()
            }
            confirmPassword.isEmpty() -> {
                rConfirmPassword.error = "Please Confirm Password"
                rConfirmPassword.requestFocus()
            }
            password != confirmPassword -> {
                rConfirmPassword.error = "Please confirm your password again"
                rConfirmPassword.requestFocus()
            }
            else -> createAccount(email, password)
        }
    }

    private fun createAccount(email: String, password: String) {
        progressBar.visibility = View.VISIBLE
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
            requireActivity()
        ) {
            when {
                !it.isSuccessful -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "SignUp Unsuccessful, Please try again", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    addUserDetails(email)
                    progressBar.visibility = View.GONE
                    delegateActivity.goToHomePage()
                    Toast.makeText(context, "SignUp Successful", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addUserDetails(email: String) {
        val firstName = rFirstName.text.toString()
        val lastName = rLastName.text.toString()
        val user: MutableMap<String, Any> = HashMap()

        user["firstName"] = firstName
        user["lastName"] = lastName
        user["emailId"] = email

        fireStore.collection("users").document(firebaseAuth.currentUser!!.uid).set(user)
    }
}