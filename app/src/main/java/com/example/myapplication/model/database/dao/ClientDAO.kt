package com.example.myapplication.model.database.dao

import androidx.room.*
import com.example.myapplication.model.database.entity.ClientMovieEntity
import com.example.myapplication.model.database.entity.ClientEntity

@Dao
interface ClientDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addClient(cliente: ClientEntity)

    @Update(entity = ClientEntity::class)
    fun updateClient(cliente: ClientEntity)

    @Query("DELETE FROM cliente WHERE cpf LIKE :cpf")
    fun removeClient(cpf: String)

    @Query("SELECT * FROM cliente WHERE cpf LIKE :cpf AND senha LIKE :password")
    fun loginClient(cpf: String, password: String): ClientEntity?

    @Query("SELECT COUNT(*) FROM cliente WHERE cpf LIKE :cpf")
    fun clientIsExist(cpf: String): Int

    @Transaction
    @Query("SELECT * FROM cliente")
    fun fetchClientAndMovie(): List<ClientMovieEntity>
}