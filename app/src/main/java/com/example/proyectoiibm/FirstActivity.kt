package com.example.proyectoiibm

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FirstActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        //Recibo valores....
        val recibo = intent.getStringExtra("clave")

        //Declaro la vista...
        val mitexto = findViewById<TextView>(R.id.txt_texto)
        mitexto.text = recibo
    }
}