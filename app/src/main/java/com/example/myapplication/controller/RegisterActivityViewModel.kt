package com.example.myapplication.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.database.LocadoraDatabase
import com.example.myapplication.model.database.entity.ClientEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivityViewModel(
    private val locadoraDatabase: LocadoraDatabase
) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _documentNotExist = MutableLiveData<Boolean>()
    val documentNotExist: LiveData<Boolean>
        get() = _documentNotExist

    private val _isRegistered = MutableLiveData<Unit>()
    val registered: LiveData<Unit>
        get() = _isRegistered

    private val clienteDao = locadoraDatabase.clientDao()

    fun checkDocument(document: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                clienteDao.clientIsExist(document)
            }.onFailure {
                println(it.stackTraceToString())
            }.onSuccess {
                if (it == 0) _documentNotExist.postValue(true) else _documentNotExist.postValue(false)
            }
        }
    }

    fun registerClient(
        document: String,
        name: String,
        age: String,
        phoneNumber: String,
        password: String,
        isAdmin: Boolean
    ) {
        kotlin.runCatching {
            ClientEntity(
                document = document,
                name = name,
                age = age.toInt(),
                phoneNumber = phoneNumber,
                password = password,
                isAdmin = isAdmin
            )
        }.onSuccess {
            doRegisterClient(clientEntity = it)
        }.onFailure {
            _error.postValue("Aconteceu um erro! Verifique os dados e tente novamente!")
        }
    }

    private fun doRegisterClient(clientEntity: ClientEntity){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                clienteDao.addClient(clientEntity)
            }.onSuccess {
                _isRegistered.postValue(it)
            }.onFailure {
                _error.postValue("Aconteceu um erro ao tentar realizar o cadastro! Tente novamente")
            }
        }
    }


}