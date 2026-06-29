package com.example.proyectoiibm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Recupera valores..
        val mail = findViewById<EditText>(R.id.txt_mail)
        val login = findViewById<Button>(R.id.btn_login)
        val registro = findViewById<Button>(R.id.btn_registro)

        login.setOnClickListener {
            val email = mail.text.toString()
            if (email.isNotEmpty()) {
                Toast.makeText(this, "Bienvenido: $email", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor ingrese su usuario", Toast.LENGTH_SHORT).show()
            }
        }

        // Al presionar el botón de registro, navegamos a FirstActivity (activity_first.xml)
        registro.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
        }
    }
}
