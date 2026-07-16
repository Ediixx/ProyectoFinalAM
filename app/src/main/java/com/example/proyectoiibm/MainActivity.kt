package com.example.proyectoiibm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyectoiibm.data.local.SanaYaDatabase
import com.example.proyectoiibm.data.repository.SanaYaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Recupera valores..
        val mail = findViewById<EditText>(R.id.txt_mail)
        val login = findViewById<Button>(R.id.btn_login)
        val registro = findViewById<Button>(R.id.btn_registro)
        val btnBack = findViewById<android.widget.ImageButton>(R.id.btn_back_main)

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        login.setOnClickListener {
            val email = mail.text.toString().trim()
            val passwordField = findViewById<EditText>(R.id.txt_pass)
            val password = passwordField.text.toString().trim()

            if (email.isEmpty()) {
                mail.error = "Por favor ingrese su usuario"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordField.error = "Por favor ingrese su contraseña"
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val db = SanaYaDatabase.getDatabase(this@MainActivity)
                val repository = SanaYaRepository(db.userDao(), db.specialtyDao(), db.doctorDao(), db.appointmentDao())
                
                val usuario = repository.iniciarSesion(email, password)

                withContext(Dispatchers.Main) {
                    if (usuario != null) {
                        Toast.makeText(this@MainActivity, "Bienvenido: ${usuario.names}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MainActivity, SecondActivity::class.java)
                        intent.putExtra("clave", usuario.names)
                        intent.putExtra("userId", usuario.id)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, "Credenciales incorrectas o usuario no existe", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Al presionar el botón de registro, navegamos a FirstActivity (activity_first.xml)
        registro.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
        }
    }
}
