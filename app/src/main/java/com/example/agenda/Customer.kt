package com.example.agenda

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Customer (
    @PrimaryKey val codigoCli: String,
    val nombreCli: String,
    val direccionCli: String,
    val telefonoCli: String
)