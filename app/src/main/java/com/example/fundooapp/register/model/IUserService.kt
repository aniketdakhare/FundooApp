package com.example.fundooapp.register.model

interface IUserService {
    fun authenticateUser(user: User, listener: (Boolean) -> Unit)
}