package com.example.agenda

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Supplier (
    @PrimaryKey val codigoProv: String,
    val nombreProv: String,
    val direccionProv: String,
    val telefonoProv: String
)
