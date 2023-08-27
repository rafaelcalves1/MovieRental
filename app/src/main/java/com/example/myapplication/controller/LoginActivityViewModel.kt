package com.example.myapplication.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.database.ClientSession
import com.example.myapplication.model.database.LocadoraDatabase
import com.example.myapplication.model.database.entity.ClientEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LoginActivityViewModel(
    private val locadoraDatabase: LocadoraDatabase
) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _cliente = MutableLiveData<ClientEntity>()
    val cliente: LiveData<ClientEntity>
        get() = _cliente

    private val clientDao = locadoraDatabase.clientDao()

    fun login(document: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                if(document.isBlank() || password.isBlank()) throw IllegalArgumentException()
                clientDao.loginClient(document, password)
            }.onSuccess {
                it?.let {
                    _cliente.postValue(it)
                    ClientSession.client = it
                } ?: _error.postValue("Cliente não encontrado")
            }.onFailure {
                _error.postValue("Cliente não encontrado")
            }
        }
    }

}