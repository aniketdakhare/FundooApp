package com.example.fundooapp.homepage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.homepage.model.IHomeService


class HomeViewModelFactory(private val homeService: IHomeService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(homeService) as T
    }
}
