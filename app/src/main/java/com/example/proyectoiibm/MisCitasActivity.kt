package com.example.proyectoiibm

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoiibm.data.local.SanaYaDatabase
import com.example.proyectoiibm.data.repository.SanaYaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MisCitasActivity : AppCompatActivity() {

    private lateinit var rvCitas: RecyclerView
    private lateinit var adapter: CitaAdapter
    private lateinit var repository: SanaYaRepository
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_citas)

        userId = intent.getLongExtra("userId", -1L)

        val db = SanaYaDatabase.getDatabase(this)
        repository = SanaYaRepository(db.userDao(), db.specialtyDao(), db.doctorDao(), db.appointmentDao())

        findViewById<ImageButton>(R.id.btn_back_mis_citas).setOnClickListener { finish() }

        rvCitas = findViewById(R.id.rv_citas)
        rvCitas.layoutManager = LinearLayoutManager(this)
        
        adapter = CitaAdapter(
            citas = emptyList(),
            onCancelarClick = { cita -> mostrarDialogoConfirmacion(cita.id) },
            onReagendarClick = { 
                val intent = Intent(this, AgendarCitaActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
            }
        )
        rvCitas.adapter = adapter

        cargarCitas()
    }

    private fun cargarCitas() {
        if (userId == -1L) return
        
        lifecycleScope.launch(Dispatchers.IO) {
            val lista = repository.obtenerCitasActivasDelUsuario(userId)
            withContext(Dispatchers.Main) {
                adapter.updateCitas(lista)
            }
        }
    }

    private fun mostrarDialogoConfirmacion(citaId: Int) {
        AlertDialog.Builder(this)
            .setTitle("Cancelar Cita")
            .setMessage("¿Estás seguro de que deseas cancelar esta cita? Esta acción no se puede deshacer.")
            .setPositiveButton("Sí, cancelar") { _, _ ->
                cancelarCita(citaId)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun cancelarCita(citaId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            repository.cancelarCita(citaId)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MisCitasActivity, "Cita cancelada con éxito", Toast.LENGTH_SHORT).show()
                cargarCitas()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cargarCitas()
    }
}
