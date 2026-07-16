package com.example.proyectoiibm.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "especialidades",
    indices = [Index(value = ["nombre"], unique = true)]
)
data class SpecialtyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val colorHex: String,
    val iconoResName: String
)
