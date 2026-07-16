package com.example.proyectoiibm

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoiibm.data.local.SanaYaDatabase
import com.example.proyectoiibm.data.repository.SanaYaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistorialActivity : AppCompatActivity() {

    private lateinit var rvHistorial: RecyclerView
    private lateinit var adapter: CitaAdapter
    private lateinit var repository: SanaYaRepository
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        userId = intent.getLongExtra("userId", -1L)

        val db = SanaYaDatabase.getDatabase(this)
        repository = SanaYaRepository(db.userDao(), db.specialtyDao(), db.doctorDao(), db.appointmentDao())

        findViewById<ImageButton>(R.id.btn_back_historial).setOnClickListener { finish() }

        rvHistorial = findViewById(R.id.rv_historial)
        rvHistorial.layoutManager = LinearLayoutManager(this)
        
        adapter = CitaAdapter(emptyList(), esHistorial = true)
        rvHistorial.adapter = adapter

        cargarHistorial()
    }

    private fun cargarHistorial() {
        if (userId == -1L) return
        
        lifecycleScope.launch(Dispatchers.IO) {
            val lista = repository.obtenerHistorialDelUsuario(userId)
            withContext(Dispatchers.Main) {
                adapter.updateCitas(lista)
            }
        }
    }
}
