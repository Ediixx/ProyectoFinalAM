package com.example.proyectoiibm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.proyectoiibm.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertar(usuario: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun buscarUsuarioPorEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    fun iniciarSesion(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun obtenerUsuarioPorId(id: Long): UserEntity?

    @Update
    fun actualizar(usuario: UserEntity)
}
