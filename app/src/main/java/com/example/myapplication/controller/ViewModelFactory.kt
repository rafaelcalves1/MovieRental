package com.example.myapplication.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.model.database.LocadoraDatabase

class MainActivityViewModelFactory(private val locadoraDatabase: LocadoraDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeActivityViewModel(locadoraDatabase = locadoraDatabase) as T
    }
}

class LoginActivityViewModelFactory(private val locadoraDatabase: LocadoraDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginActivityViewModel(locadoraDatabase = locadoraDatabase) as T
    }
}

class RegisterActivityViewModelFactory(private val locadoraDatabase: LocadoraDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterActivityViewModel(locadoraDatabase = locadoraDatabase) as T
    }
}