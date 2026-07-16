package com.example.proyectoiibm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.proyectoiibm.data.local.entity.AppointmentEntity

@Dao
interface AppointmentDao {

    @Insert
    fun insertar(cita: AppointmentEntity): Long

    @Query("SELECT * FROM citas WHERE usuarioId = :usuarioId AND estado = 'Agendada' ORDER BY fechaHoraMillis ASC")
    fun obtenerCitasActivasDelUsuario(usuarioId: Long): List<AppointmentEntity>

    @Query("SELECT * FROM citas WHERE usuarioId = :usuarioId AND estado != 'Agendada' ORDER BY fechaHoraMillis DESC")
    fun obtenerHistorialDelUsuario(usuarioId: Long): List<AppointmentEntity>

    @Query("SELECT * FROM citas WHERE id = :citaId LIMIT 1")
    fun obtenerPorId(citaId: Int): AppointmentEntity?

    @Query("UPDATE citas SET estado = :nuevoEstado WHERE id = :citaId")
    fun actualizarEstado(citaId: Int, nuevoEstado: String)

    @Query("DELETE FROM citas WHERE id = :citaId")
    fun eliminar(citaId: Int)
}
