package com.example.myapplication.model.database.dao

import androidx.room.*
import com.example.myapplication.model.database.entity.MovieEntity

@Dao
interface FilmeDAO {

    @Query("SELECT * FROM filme")
    suspend fun getMovieList(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(filme: MovieEntity)

    @Query("DELETE FROM filme WHERE filmeId LIKE :id")
    suspend fun removeMovie(id: Int)

    @Query("UPDATE Filme SET alugado = true, userCpf = :cpfCliente WHERE filmeId = :idFilme")
    suspend fun rentMovie(idFilme: Int, cpfCliente: String)

    @Query("UPDATE Filme SET alugado = false, userCpf = null WHERE filmeId = :idFilme AND userCpf = :cpfCliente")
    suspend fun returnRentedMovie(idFilme: Int, cpfCliente: String)

}