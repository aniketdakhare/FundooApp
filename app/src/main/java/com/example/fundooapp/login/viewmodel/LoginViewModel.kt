package com.example.fundooapp.login.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.IUserService
import com.example.fundooapp.model.User
import com.example.fundooapp.util.Failed
import com.example.fundooapp.util.FailingReason.*
import com.example.fundooapp.util.Loading
import com.example.fundooapp.util.Status
import com.example.fundooapp.util.Succeed
import com.facebook.AccessToken

class LoginViewModel(private val userService: IUserService) : ViewModel() {

    init {
        fetchProfileImageUri()
    }

    private val _userAuthenticationStatus = MutableLiveData<Status>()
    val userAuthenticationStatus = _userAuthenticationStatus as LiveData<Status>

    private val _resetPasswordStatus = MutableLiveData<String>()
    val resetPasswordStatus = _resetPasswordStatus as LiveData<String>

    private val _facebookLoginStatus = MutableLiveData<Status>()
    val facebookLoginStatus = _facebookLoginStatus as LiveData<Status>

    private val _userDetails = MutableLiveData<User>()
    val userDetails = _userDetails as LiveData<User>

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri = _imageUri as LiveData<Uri>

    fun authenticateUser(email: String, password: String) {
        _userAuthenticationStatus.value = Loading
        when {
            email.isEmpty() -> _userAuthenticationStatus.value = Failed("Please Enter Email Id", EMAIL)
            password.isEmpty() -> _userAuthenticationStatus.value = Failed("Please Enter Password", PASSWORD)
            else -> {
                userService.authenticateUser(email, password) {
                    when (it) {
                        false -> _userAuthenticationStatus.value = Failed(FAIL_MSG, OTHER)
                        true -> _userAuthenticationStatus.value = Succeed(SUCCESS_MSG)
                    }
                }
            }
        }
    }

    fun resetPassword(emailId: String) {
        userService.resetPassword(emailId) {
            when (it) {
                true -> _resetPasswordStatus.value = "Reset link sent to given email Id"
                false -> _resetPasswordStatus.value = "ERROR !! Reset link is not sent."
            }
        }
    }

    fun getResetPasswordContent(): Pair<String, String> {
        return Pair("Reset Password ?", "Enter Your email Id to receive the password reset link.")
    }

    fun loginWithFacebook(token: AccessToken) {
        userService.facebookLogin(token) { user: User, status: Boolean ->
            when (status) {
                false -> _facebookLoginStatus.value = Failed(FAIL_MSG, OTHER)
                true -> {
                    _facebookLoginStatus.value = Succeed(SUCCESS_MSG)
                    _userDetails.value = user
                }
            }
        }
    }

    private fun fetchProfileImageUri() {
        userService.getProfileImage {
            _imageUri.value = it
        }
    }

    companion object {
        private const val SUCCESS_MSG: String = "Login Successful."
        private const val FAIL_MSG: String = "Login Unsuccessful."
    }
}