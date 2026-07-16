package com.example.proyectoiibm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyectoiibm.data.local.SanaYaDatabase
import com.example.proyectoiibm.data.repository.SanaYaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilActivity : AppCompatActivity() {

    private lateinit var repository: SanaYaRepository
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        userId = intent.getLongExtra("userId", -1L)

        val db = SanaYaDatabase.getDatabase(this)
        repository = SanaYaRepository(db.userDao(), db.specialtyDao(), db.doctorDao(), db.appointmentDao())

        findViewById<ImageButton>(R.id.btn_back_perfil).setOnClickListener { finish() }

        findViewById<Button>(R.id.btn_cerrar_sesion).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        cargarDatos()
    }

    private fun cargarDatos() {
        if (userId == -1L) return

        lifecycleScope.launch(Dispatchers.IO) {
            val usuario = repository.obtenerUsuarioPorId(userId)
            withContext(Dispatchers.Main) {
                usuario?.let {
                    findViewById<TextView>(R.id.txt_perfil_nombre).text = "${it.names} ${it.lastName}"
                    findViewById<TextView>(R.id.txt_perfil_email).text = it.email
                    findViewById<TextView>(R.id.txt_perfil_fecha).text = it.birthDate
                }
            }
        }
    }
}
