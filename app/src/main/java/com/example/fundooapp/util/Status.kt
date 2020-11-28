package com.example.fundooapp.util

sealed class Status
data class Succeed(val message: String): Status()
data class Failed(val message: String, val reason: FailingReason): Status()
object Loading : Status()
