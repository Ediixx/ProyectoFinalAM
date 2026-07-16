package com.example.proyectoiibm.data.repository

import com.example.proyectoiibm.data.local.dao.AppointmentDao
import com.example.proyectoiibm.data.local.dao.DoctorDao
import com.example.proyectoiibm.data.local.dao.SpecialtyDao
import com.example.proyectoiibm.data.local.dao.UserDao
import com.example.proyectoiibm.data.local.entity.AppointmentEntity
import com.example.proyectoiibm.data.local.entity.DoctorEntity
import com.example.proyectoiibm.data.local.entity.SpecialtyEntity
import com.example.proyectoiibm.data.local.entity.UserEntity

class SanaYaRepository(
    private val userDao: UserDao,
    private val specialtyDao: SpecialtyDao,
    private val doctorDao: DoctorDao,
    private val appointmentDao: AppointmentDao
) {

    // ---------- Usuarios (Login / Registro) ----------

    suspend fun registrarUsuario(usuario: UserEntity): Result<Long> {
        return try {
            val existente = userDao.buscarUsuarioPorEmail(usuario.email)
            if (existente != null) {
                Result.failure(Exception("Ya existe una cuenta registrada con ese correo"))
            } else {
                Result.success(userDao.insertar(usuario))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun iniciarSesion(email: String, password: String): UserEntity? {
        return userDao.iniciarSesion(email, password)
    }

    suspend fun obtenerUsuarioPorEmail(email: String): UserEntity? {
        return userDao.buscarUsuarioPorEmail(email)
    }

    suspend fun obtenerUsuarioPorId(id: Long): UserEntity? {
        return userDao.obtenerUsuarioPorId(id)
    }

    // ---------- Especialidades ----------

    suspend fun obtenerEspecialidades(): List<SpecialtyEntity> {
        return specialtyDao.obtenerEspecialidadesConDoctores()
    }

    // ---------- Doctores ----------

    suspend fun obtenerDoctoresPorEspecialidad(especialidadId: Int): List<DoctorEntity> {
        return doctorDao.obtenerPorEspecialidad(especialidadId)
    }

    // ---------- Citas ----------

    suspend fun agendarCita(cita: AppointmentEntity): Long {
        return appointmentDao.insertar(cita)
    }

    suspend fun obtenerCitasActivasDelUsuario(usuarioId: Long): List<AppointmentEntity> {
        return appointmentDao.obtenerCitasActivasDelUsuario(usuarioId)
    }

    suspend fun obtenerHistorialDelUsuario(usuarioId: Long): List<AppointmentEntity> {
        return appointmentDao.obtenerHistorialDelUsuario(usuarioId)
    }

    suspend fun cancelarCita(citaId: Int) {
        appointmentDao.actualizarEstado(citaId, "Cancelada")
    }
}
