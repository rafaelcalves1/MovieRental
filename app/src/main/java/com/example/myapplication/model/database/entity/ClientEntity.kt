package com.example.myapplication.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cliente")
data class ClientEntity(
    @ColumnInfo(name = "nome")
    val name: String,

    @ColumnInfo(name = "idade")
    val age: Int,

    @ColumnInfo(name = "telefone")
    val phoneNumber: String,

    @ColumnInfo(name = "admin")
    val isAdmin: Boolean,

    @ColumnInfo(name = "senha")
    val password: String,

    @PrimaryKey
    @ColumnInfo(name = "cpf")
    val document: String
)

