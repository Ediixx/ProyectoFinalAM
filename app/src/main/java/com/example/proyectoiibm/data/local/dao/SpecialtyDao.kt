package com.example.proyectoiibm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectoiibm.data.local.entity.SpecialtyEntity

@Dao
interface SpecialtyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertarTodas(especialidades: List<SpecialtyEntity>)

    @Query("SELECT * FROM especialidades ORDER BY nombre ASC")
    fun obtenerTodas(): List<SpecialtyEntity>

    @Query("SELECT * FROM especialidades WHERE id IN (SELECT DISTINCT especialidadId FROM doctores) ORDER BY nombre ASC")
    fun obtenerEspecialidadesConDoctores(): List<SpecialtyEntity>

    @Query("SELECT * FROM especialidades WHERE id = :id LIMIT 1")
    fun obtenerPorId(id: Int): SpecialtyEntity?
}
