package com.example.myapplication.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.database.LocadoraDatabase
import com.example.myapplication.model.database.entity.ClientEntity
import com.example.myapplication.model.database.entity.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivityViewModel(
    private val locadoraDatabase: LocadoraDatabase
) : ViewModel() {

    private val _moviesList = MutableLiveData<List<MovieEntity>>()
    val moviesList: LiveData<List<MovieEntity>>
        get() = _moviesList

    private val _rented = MutableLiveData<String>()
    val rented: LiveData<String>
        get() = _rented

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val filmesDAO = locadoraDatabase.movieDao()

    fun fetchMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                filmesDAO.getMovieList()
            }.onSuccess {
                _moviesList.postValue(it)
            }.onFailure {
                _error.postValue("Ocorreu um erro ao listar os filmes")
            }
        }
    }

    fun rentMovie(movieEntity: MovieEntity, clientEntity: ClientEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                if(movieEntity.isRented) return@runCatching
                filmesDAO.rentMovie(idFilme = movieEntity.id, cpfCliente = clientEntity.document)
            }.onSuccess {
                fetchMovies()
            }.onFailure {
                _error.postValue("Ocorreu um erro ao tentar alugar o filme")
            }
        }
    }

    fun returnRentedMovie(movieEntity: MovieEntity, clientEntity: ClientEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                if(movieEntity.userDocument != clientEntity.document){
                    _error.postValue("Você pode devolver o filme alugado por outra pessoa!")
                    return@runCatching
                }
                filmesDAO.returnRentedMovie(idFilme = movieEntity.id, cpfCliente = clientEntity.document)
            }.onSuccess {
                fetchMovies()
            }.onFailure {
                _error.postValue("Ocorreu um erro ao tentar devolver o filme")
            }
        }
    }

    fun deleteMovie(filmeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                filmesDAO.removeMovie(filmeId)
            }.onSuccess {
                fetchMovies()
            }.onFailure {
                _error.postValue("Ocorreu um erro ao tentar deletar o filme")
            }
        }
    }

    fun addMovie(nomeFilme: String, anoFilme: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                val movieEntity = MovieEntity(
                    name = nomeFilme,
                    yearOfMovie = anoFilme,
                    isRented = false,
                    userDocument = null
                )
                filmesDAO.addMovie(movieEntity)
            }.onSuccess {
                fetchMovies()
            }.onFailure {
                println(it.stackTrace)
            }
        }
    }
}