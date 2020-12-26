package com.example.fundooapp.register.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.R
import com.example.fundooapp.databinding.FragmentRegisterBinding
import com.example.fundooapp.fundoofirebaseauth.LoginService
import com.example.fundooapp.model.User
import com.example.fundooapp.model.UserService
import com.example.fundooapp.register.viewmodel.RegisterViewModel
import com.example.fundooapp.register.viewmodel.RegisterViewModelFactory
import com.example.fundooapp.util.Failed
import com.example.fundooapp.util.FailingReason.*
import com.example.fundooapp.util.Loading
import com.example.fundooapp.util.Succeed
import com.example.fundooapp.viewmodel.SharedViewModel
import com.example.fundooapp.viewmodel.SharedViewModelFactory

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        registerViewModel = ViewModelProvider(
            this,
            RegisterViewModelFactory(UserService())
        ).get(RegisterViewModel::class.java)
        sharedViewModel = ViewModelProvider(
            requireActivity(), SharedViewModelFactory(LoginService())
        )[SharedViewModel::class.java]
        binding.registerViewModel = registerViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUpButton.setOnClickListener {
            checkRegistrationDetails()
        }
        goToLoginPage()
    }

    private fun goToLoginPage() {
        binding.registerTextView.setOnClickListener {
            sharedViewModel.setGoToLoginPageStatus(true)
            sharedViewModel.setGoToRegisterPageStatus(false)
        }
    }

    private fun checkRegistrationDetails() {
        val email = binding.registerEmail.text.toString().trim()
        val password = binding.registerPassword.text.toString().trim()
        val confirmPassword = binding.registerConfirmPassword.text.toString().trim()
        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()

        Log.e("Pwd: $password", ":: Cpwd: $confirmPassword")
        registerViewModel.registerUser(
            User(
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password,
                confirmPassword = confirmPassword,
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/34/PICA.jpg/900px-PICA.jpg"
            )
        )

        registerViewModel.userRegistrationStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Failed -> {
                    when (it.reason) {
                        EMAIL -> {
                            binding.registerProgressBar.visibility = View.GONE
                            binding.registerEmail.error = it.message
                            binding.registerEmail.requestFocus()
                        }
                        PASSWORD -> {
                            binding.registerProgressBar.visibility = View.GONE
                            binding.registerPassword.error = it.message
                            binding.registerPassword.requestFocus()
                        }
                        CONFIRM_PASSWORD -> {
                            binding.registerProgressBar.visibility = View.GONE
                            binding.registerConfirmPassword.error = it.message
                            binding.registerConfirmPassword.requestFocus()
                        }
                        FIRST_NAME -> {
                            binding.registerProgressBar.visibility = View.GONE
                            binding.firstName.error = it.message
                            binding.firstName.requestFocus()
                        }
                        LAST_NAME -> {
                            binding.registerProgressBar.visibility = View.GONE
                            binding.lastName.error = it.message
                            binding.lastName.requestFocus()
                        }
                        OTHER -> {
                            binding.registerProgressBar.visibility = View.GONE
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is Succeed -> registerUser(it.message)
                Loading -> binding.registerProgressBar.visibility = View.VISIBLE
            }
        })
    }

    private fun registerUser(message: String) {
//        sharedViewModel.fetchUserDetails()
        binding.registerProgressBar.visibility = View.GONE
        sharedViewModel.setGoToHomePageStatus(true)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}