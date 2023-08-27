package com.example.myapplication.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filme")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "filmeId")
    val id: Int = 0,

    @ColumnInfo(name = "nome")
    val name: String,

    @ColumnInfo(name = "anoFilme")
    val yearOfMovie: String,

    @ColumnInfo(name = "alugado")
    val isRented: Boolean,

    @ColumnInfo(name= "userCpf")
    val userDocument: String?
)



