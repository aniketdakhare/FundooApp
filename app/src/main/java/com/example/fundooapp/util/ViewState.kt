package com.example.fundooapp.util

sealed class ViewState<T> {
    data class Loading<T>(val preloadedData: T?): ViewState<T>()
    data class Success<T>(val data: T): ViewState<T>()
    data class Failure<T>(val error: String = "Something went wrong"): ViewState<T>()
}