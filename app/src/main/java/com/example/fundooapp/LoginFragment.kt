package com.example.fundooapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var lEmail: EditText
    private lateinit var lPassword: EditText
    private lateinit var login: Button
    private lateinit var facebookLoginButton: LoginButton
    private lateinit var notRegistered: TextView
    private lateinit var forgotPassword: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var delegateActivity: DelegateActivity
    private lateinit var progressBar: ProgressBar
    private lateinit var callBackManager: CallbackManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRegister(view)
        login.setOnClickListener {
            checkLoginDetails()
        }
        goToSignUpPage()
        actionForForgetPassword()
        facebookLogin()
    }

    private fun actionForForgetPassword() {
        forgotPassword.setOnClickListener { resetUserPassword(it) }
    }

    private fun initRegister(view: View) {
        lEmail = view.findViewById(R.id.loginEmailAddress)
        lPassword = view.findViewById(R.id.loginPassword)
        login = view.findViewById(R.id.loginButton)
        notRegistered = view.findViewById(R.id.loginTextView)
        firebaseAuth = FirebaseAuth.getInstance()
        progressBar = view.findViewById(R.id.loginProgressBar)
        forgotPassword = view.findViewById(R.id.forgotPassword)
        facebookLoginButton = view.findViewById(R.id.facebook_login_button)
        callBackManager = CallbackManager.Factory.create()
    }

    fun initiateActivity(mainActivity: DelegateActivity)
    {
        this.delegateActivity = mainActivity
    }

    private fun checkLoginDetails() {
        val email = lEmail.text.toString().trim()
        val password = lPassword.text.toString().trim()

        when {
            email.isEmpty() -> {
                lEmail.error = "Please Enter Email Id"
                lEmail.requestFocus()
            }
            password.isEmpty() -> {
                lPassword.error = "Please Enter Password"
                lPassword.requestFocus()
            }
            else -> loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String){
        progressBar.visibility = View.VISIBLE
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
        ){
            when {
                !it.isSuccessful -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Login Unsuccessful.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    delegateActivity.goToHomePage()
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToSignUpPage() {
        notRegistered.setOnClickListener {
            delegateActivity.registerUser()
        }
    }

    private fun resetUserPassword(view: View)
    {
        val emailId = EditText(view.context)
        val passwordResetDialog = AlertDialog.Builder(view.context).apply {
            setTitle("Reset Password ?")
            setMessage("Enter Your email Id to receive the password reset link.")
            setView(emailId)
        }
        passwordResetDialog.setPositiveButton("Yes") { dialogInterface: DialogInterface, _: Int ->
            firebaseAuth.sendPasswordResetEmail(emailId.text.toString()).addOnSuccessListener {
                Toast.makeText(context, "Reset link sent to given email Id", Toast.LENGTH_SHORT).show()
                dialogInterface.dismiss()
            }.addOnFailureListener{
                Toast.makeText(
                    context,
                    "ERROR !! Reset link is not sent\n" + it.message,
                    Toast.LENGTH_SHORT
                ).show()
                dialogInterface.dismiss()
            }
        }.setCancelable(false)
        passwordResetDialog.setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }
        passwordResetDialog.create().show()
    }

    private fun facebookLogin() {
        facebookLoginButton.fragment = this
        facebookLoginButton.setReadPermissions("email", "public_profile")

        facebookLoginButton.registerCallback(
            callBackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    if (loginResult != null) {
                        handleFacebookToken(loginResult.accessToken)
                        Log.e(TAG, "onSuccess: ")
                    }
                }

                override fun onCancel() {}
                override fun onError(exception: FacebookException) {}
            })
    }

    private fun handleFacebookToken(token: AccessToken) {
        val credentials = FacebookAuthProvider.getCredential(token.token)
        progressBar.visibility = View.VISIBLE
        firebaseAuth.signInWithCredential(credentials).addOnCompleteListener{
            if (!it.isSuccessful) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Login Unsuccessful.", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "handleFacebookToken: Failed")
            }
            else {
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                delegateActivity.goToHomePage()
                progressBar.visibility = View.GONE
                it.result!!.user!!.uid
                Log.e(TAG, "handleFacebookToken: Succeed")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callBackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}