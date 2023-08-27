package com.example.myapplication.model.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ClientMovieEntity(
    @Embedded val cliente: ClientEntity,
    @Relation(
        parentColumn = "cpf",
        entityColumn = "userCpf"
    )
    val movieEntity: MovieEntity
)