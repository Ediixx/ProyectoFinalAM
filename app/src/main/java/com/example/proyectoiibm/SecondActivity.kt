package com.example.proyectoiibm

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.LinearLayout

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        // Fecha Dinamica
        val sdf = java.text.SimpleDateFormat("EEEE, d 'de' MMMM", java.util.Locale("es", "ES"))
        findViewById<TextView>(R.id.txt_fecha).text = sdf.format(java.util.Date())
        // Buscamos la Toolbar y la configuramos
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        
        // Recibimos el valor
        val recibo = intent.getStringExtra("clave")
        val userId = intent.getLongExtra("userId", -1L)

        // Buscamos el ID correcto
        val nombreView = findViewById<TextView>(R.id.txt_nombre)
        // Tarjeta "Agendar cita" del home
        val lyAgendarCita = findViewById<LinearLayout>(R.id.ly_agendar_cita)
        lyAgendarCita.setOnClickListener {
            val intent = Intent(this, AgendarCitaActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        findViewById<LinearLayout>(R.id.ly_mis_citas).setOnClickListener {
            val intent = Intent(this, MisCitasActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        findViewById<LinearLayout>(R.id.ly_historial).setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        findViewById<LinearLayout>(R.id.ly_perfil).setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        // Mostramos el nombre (o un texto por defecto si viene vacío)
        nombreView.text = recibo ?: "Usuario"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val userId = intent.getLongExtra("userId", -1L)
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.li_perfil -> {
                val intent = Intent(this, PerfilActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
                true
            }
            R.id.li_cita -> {
                val intent = Intent(this, AgendarCitaActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
                true
            }
            R.id.li_mis_citas -> {
                val intent = Intent(this, MisCitasActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
                true
            }
            R.id.li_historial -> {
                val intent = Intent(this, HistorialActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
                true
            }
            R.id.li_salir -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}