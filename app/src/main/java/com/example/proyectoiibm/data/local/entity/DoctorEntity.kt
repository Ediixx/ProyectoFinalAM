package com.example.proyectoiibm.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "doctores",
    foreignKeys = [
        ForeignKey(
            entity = SpecialtyEntity::class,
            parentColumns = ["id"],
            childColumns = ["especialidadId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("especialidadId"),
        Index(value = ["nombre", "especialidadId"], unique = true)
    ]
)
data class DoctorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val especialidadId: Int,
    val direccion: String,
    val distanciaKm: Double
)
