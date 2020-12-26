package com.example.fundooapp.login.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.R
import com.example.fundooapp.databinding.FragmentLoginBinding
import com.example.fundooapp.fundoofirebaseauth.LoginService
import com.example.fundooapp.login.viewmodel.LoginViewModel
import com.example.fundooapp.login.viewmodel.LoginViewModelFactory
import com.example.fundooapp.model.UserService
import com.example.fundooapp.util.Failed
import com.example.fundooapp.util.FailingReason.*
import com.example.fundooapp.util.Loading
import com.example.fundooapp.util.Succeed
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var passwordResetDialog: AlertDialog.Builder
    private lateinit var callBackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        loginViewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(UserService(), LoginService())
        ).get(LoginViewModel::class.java)
        binding.lifecycleOwner = this
        callBackManager = CallbackManager.Factory.create()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedViewModel = ViewModelProvider(
            requireActivity(), SharedViewModelFactory(LoginService())
        )[SharedViewModel::class.java]
        binding.loginViewModel = loginViewModel
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            checkLoginDetails()
        }
        goToSignUpPage()
        actionForForgetPassword()
        facebookLogin()
    }

    private fun actionForForgetPassword() {
        binding.forgotPassword.setOnClickListener { resetUserPassword(it) }
    }

    private fun checkLoginDetails() {
        val email = binding.loginEmailAddress.text.toString().trim()
        val password = binding.loginPassword.text.toString().trim()

        loginViewModel.authenticateUser(email, password)

        loginViewModel.userAuthenticationStatus.observe(viewLifecycleOwner, Observer {
            Log.e(TAG, "checkLoginDetails: ${it}" )
            when (it) {
                is Failed -> {
                    when (it.reason) {
                        EMAIL -> {
                            binding.loginProgressBar.visibility = View.GONE
                            binding.loginEmailAddress.error = it.message
                            binding.loginEmailAddress.requestFocus()
                        }
                        PASSWORD -> {
                            binding.loginProgressBar.visibility = View.GONE
                            binding.loginPassword.error = it.message
                            binding.loginPassword.requestFocus()
                        }
                        OTHER -> {
                            binding.loginProgressBar.visibility = View.GONE
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
                    }
                }
                is Succeed -> loginUser(it)
                Loading -> binding.loginProgressBar.visibility = View.VISIBLE
            }
        })
    }

    private fun loginUser(status: Succeed) {
        sharedViewModel.fetchUserDetails(status.localId, status.idToken)
        sharedViewModel.userDetails.observe(viewLifecycleOwner, {
            binding.loginProgressBar.visibility = View.GONE
            sharedViewModel.setGoToHomePageStatus(true)
            Toast.makeText(context, status.message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun goToSignUpPage() {
        binding.loginTextView.setOnClickListener {
            sharedViewModel.setGoToRegisterPageStatus(true)
            sharedViewModel.setGoToLoginPageStatus(false)
        }
    }

    private fun resetUserPassword(view: View) {
        val emailId = EditText(view.context)
        val content: Pair<String, String> = loginViewModel.getResetPasswordContent()
        passwordResetDialog = AlertDialog.Builder(view.context).apply {
            setTitle(content.first)
            setMessage(content.second)
            setView(emailId)
        }
        passwordResetDialog.setPositiveButton("Reset") { dialogInterface: DialogInterface, _: Int ->
            loginViewModel.resetPassword(emailId.text.toString())
            dialogInterface.dismiss()
        }.setCancelable(false)

        passwordResetDialog.setNegativeButton("Cancel") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }
        passwordResetDialog.create().show()

        loginViewModel.resetPasswordStatus.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun facebookLogin() {
        binding.facebookLoginButton.fragment = this
        binding.facebookLoginButton.setPermissions("email", "public_profile")

        binding.facebookLoginButton.registerCallback(
            callBackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    if (loginResult != null) {
                        handleFacebookToken(loginResult.accessToken)
                    }
                }

                override fun onCancel() {}
                override fun onError(exception: FacebookException) {}
            })
    }

    private fun handleFacebookToken(token: AccessToken) {
        binding.loginProgressBar.visibility = View.VISIBLE
        loginViewModel.loginWithFacebook(token)
        loginViewModel.facebookLoginStatus.observe(viewLifecycleOwner, { status ->
            when (status) {
                is Succeed -> {
                    loginViewModel.userDetails.observe(viewLifecycleOwner, {
                        sharedViewModel.setUserDetails(it)
                        binding.loginProgressBar.visibility = View.GONE
                        Toast.makeText(context, status.message, Toast.LENGTH_SHORT).show()
                        sharedViewModel.setGoToHomePageStatus(true)
                    })
                }
                is Failed -> {
                    binding.loginProgressBar.visibility = View.GONE
                    Toast.makeText(context, status.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callBackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}