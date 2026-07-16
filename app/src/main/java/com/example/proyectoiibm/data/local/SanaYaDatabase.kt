package com.example.proyectoiibm.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.proyectoiibm.data.local.dao.AppointmentDao
import com.example.proyectoiibm.data.local.dao.DoctorDao
import com.example.proyectoiibm.data.local.dao.SpecialtyDao
import com.example.proyectoiibm.data.local.dao.UserDao
import com.example.proyectoiibm.data.local.entity.AppointmentEntity
import com.example.proyectoiibm.data.local.entity.DoctorEntity
import com.example.proyectoiibm.data.local.entity.SpecialtyEntity
import com.example.proyectoiibm.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        SpecialtyEntity::class,
        DoctorEntity::class,
        AppointmentEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class SanaYaDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun specialtyDao(): SpecialtyDao
    abstract fun doctorDao(): DoctorDao
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        @Volatile
        private var INSTANCE: SanaYaDatabase? = null

        fun getDatabase(context: Context): SanaYaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    SanaYaDatabase::class.java,
                    "sanaya_db"
                )
                    .addCallback(SeedCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instancia
                instancia
            }
        }
    }

    private class SeedCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            seedData(context)
        }

        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
            seedData(context)
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            seedData(context)
        }

        private fun seedData(context: Context) {
            CoroutineScope(Dispatchers.IO).launch {
                val database = getDatabase(context)
                
                // Verificar si ya hay datos para no duplicar si se llama por onOpen o similar
                val existing = database.specialtyDao().obtenerTodas()
                if (existing.isNotEmpty()) return@launch

                val especialidades = listOf(
                    SpecialtyEntity(
                        nombre = "Médico General",
                        colorHex = "#3B82F6",
                        iconoResName = "outline_medical_services_24"
                    ),
                    SpecialtyEntity(
                        nombre = "Cardiología",
                        colorHex = "#EF4444",
                        iconoResName = "outline_cardiology_24"
                    ),
                    SpecialtyEntity(
                        nombre = "Traumatología",
                        colorHex = "#F97316",
                        iconoResName = "outline_accessibility_new_24"
                    ),
                    SpecialtyEntity(
                        nombre = "Oftalmología",
                        colorHex = "#A855F7",
                        iconoResName = "outline_visibility_24"
                    ),
                    SpecialtyEntity(
                        nombre = "Neurología",
                        colorHex = "#6366F1",
                        iconoResName = "outline_neurology_24"
                    ),
                    SpecialtyEntity(
                        nombre = "Vacunación",
                        colorHex = "#22C55E",
                        iconoResName = "outline_vaccines_24"
                    ),
                    SpecialtyEntity(
                        nombre = "Odontología",
                        colorHex = "#00BCD4",
                        iconoResName = "outline_dentistry_24"
                    ),
                    SpecialtyEntity(
                        nombre = "Pediatría",
                        colorHex = "#FF9800",
                        iconoResName = "outline_pediatrics_24"
                    )
                )
                database.specialtyDao().insertarTodas(especialidades)

                val especialidadesGuardadas = database.specialtyDao().obtenerTodas()

                val idMedicoGeneral = especialidadesGuardadas.first { it.nombre == "Médico General" }.id
                val idCardiologia = especialidadesGuardadas.first { it.nombre == "Cardiología" }.id
                val idTraumatologia = especialidadesGuardadas.first { it.nombre == "Traumatología" }.id
                val idOftalmologia = especialidadesGuardadas.first { it.nombre == "Oftalmología" }.id
                val idNeurologia = especialidadesGuardadas.first { it.nombre == "Neurología" }.id
                val idVacunacion = especialidadesGuardadas.first { it.nombre == "Vacunación" }.id
                val idOdontologia = especialidadesGuardadas.first { it.nombre == "Odontología" }.id
                val idPediatria = especialidadesGuardadas.first { it.nombre == "Pediatría" }.id

                val doctores = listOf(
                    // -- Médicos Generales --
                    DoctorEntity(
                        nombre = "Dr. Juan Pérez",
                        especialidadId = idMedicoGeneral,
                        direccion = "Av. Principal #123, Centro",
                        distanciaKm = 0.5
                    ),
                    DoctorEntity(
                        nombre = "Dra. María González",
                        especialidadId = idMedicoGeneral,
                        direccion = "Calle Flores #456, Norte",
                        distanciaKm = 1.2
                    ),
                    DoctorEntity(
                        nombre = "Dr. Roberto Díaz",
                        especialidadId = idMedicoGeneral,
                        direccion = "Av. de la Salud #789",
                        distanciaKm = 2.5
                    ),
                    // -- Cardiólogos --
                    DoctorEntity(
                        nombre = "Dr. Carlos Ruiz",
                        especialidadId = idCardiologia,
                        direccion = "Clínica del Corazón - Piso 3",
                        distanciaKm = 3.4
                    ),
                    DoctorEntity(
                        nombre = "Dra. Patricia Herrera",
                        especialidadId = idCardiologia,
                        direccion = "Hospital Central - Consultorio 12",
                        distanciaKm = 1.8
                    ),
                    // -- Traumatólogos --
                    DoctorEntity(
                        nombre = "Dr. Fernando Soto",
                        especialidadId = idTraumatologia,
                        direccion = "Centro de Traumatología Avanzada",
                        distanciaKm = 4.2
                    ),
                    DoctorEntity(
                        nombre = "Dra. Laura Méndez",
                        especialidadId = idTraumatologia,
                        direccion = "Av. Olímpica #55",
                        distanciaKm = 0.9
                    ),
                    // -- Oftalmólogos --
                    DoctorEntity(
                        nombre = "Dr. Jorge Luna",
                        especialidadId = idOftalmologia,
                        direccion = "Óptica Visión Clara - Local 4",
                        distanciaKm = 2.0
                    ),
                    DoctorEntity(
                        nombre = "Dra. Elena Ríos",
                        especialidadId = idOftalmologia,
                        direccion = "Clínica de Ojos Santa Lucía",
                        distanciaKm = 5.1
                    ),
                    // -- Neurólogos --
                    DoctorEntity(
                        nombre = "Dr. Gabriel Paz",
                        especialidadId = idNeurologia,
                        direccion = "Instituto del Cerebro",
                        distanciaKm = 6.3
                    ),
                    DoctorEntity(
                        nombre = "Dra. Sofía Vargas",
                        especialidadId = idNeurologia,
                        direccion = "Torre Médica Life - Piso 10",
                        distanciaKm = 3.7
                    ),
                    // -- Vacunación --
                    DoctorEntity(
                        nombre = "Centro de Salud Norte",
                        especialidadId = idVacunacion,
                        direccion = "Av. Amazonas y Colón",
                        distanciaKm = 0.3
                    ),
                    DoctorEntity(
                        nombre = "Punto de Vacunación Express",
                        especialidadId = idVacunacion,
                        direccion = "Centro Comercial El Bosque",
                        distanciaKm = 4.5
                    ),
                    // -- Odontólogos --
                    DoctorEntity(
                        nombre = "Dra. Ana López",
                        especialidadId = idOdontologia,
                        direccion = "Torre Médica 2 - Consultorio 4B",
                        distanciaKm = 2.1
                    ),
                    DoctorEntity(
                        nombre = "Dr. Mario Castro",
                        especialidadId = idOdontologia,
                        direccion = "Dental Care - Av. Shyris",
                        distanciaKm = 1.5
                    ),
                    // -- Pediatras --
                    DoctorEntity(
                        nombre = "Dr. Luis Martínez",
                        especialidadId = idPediatria,
                        direccion = "Centro Pediátrico Sonrisas",
                        distanciaKm = 4.0
                    ),
                    DoctorEntity(
                        nombre = "Dra. Isabel Cueva",
                        especialidadId = idPediatria,
                        direccion = "Clínica de la Mujer y el Niño",
                        distanciaKm = 2.8
                    )
                )
                database.doctorDao().insertarTodos(doctores)
            }
        }
    }
}
