package com.example.fundooapp.register.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.model.IUserService
import com.example.fundooapp.model.User
import com.example.fundooapp.util.Failed
import com.example.fundooapp.util.FailingReason.*
import com.example.fundooapp.util.Status
import com.example.fundooapp.util.Succeed

class RegisterViewModel(private val userService: IUserService): ViewModel() {
    private val _userRegistrationStatus = MutableLiveData<Status>()
    val userRegistrationStatus = _userRegistrationStatus as LiveData<Status>

    fun registerUser(user: User) {
        when {
            user.firstName.isEmpty() -> {
                _userRegistrationStatus.value = Failed("Please Enter your first name.", FIRST_NAME)
            }
            user.lastName.isEmpty() -> {
                _userRegistrationStatus.value = Failed("Please Enter your last name.", LAST_NAME)
            }
            user.email.isEmpty() -> {
                _userRegistrationStatus.value = Failed("Please Enter Email Id", EMAIL)
            }
            user.password.isEmpty() -> {
                _userRegistrationStatus.value = Failed("Please Enter Password", EMAIL)
            }
            user.confirmPassword.isEmpty() -> {
                _userRegistrationStatus.value = Failed("Please Confirm Password", CONFIRM_PASSWORD)
            }
            user.password != user.confirmPassword -> {
                _userRegistrationStatus.value =
                    Failed("Please confirm your password again", CONFIRM_PASSWORD)
            }
            else -> {
                userService.registerUser(user) {
                    when (it) {
                        false -> _userRegistrationStatus.value = Failed("Registration unsuccessful, Please try again", OTHER)
                        true -> _userRegistrationStatus.value = Succeed("Registration Successful")
                    }
                }
            }
        }
    }
}
