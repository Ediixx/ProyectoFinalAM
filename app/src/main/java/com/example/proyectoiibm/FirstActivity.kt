package com.example.proyectoiibm

import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import java.util.Calendar

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        val btnRegistrar = findViewById<Button>(R.id.btn_finalizar_registro)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        
        val txtNombres = findViewById<EditText>(R.id.txt_nombres)
        val txtApellidos = findViewById<EditText>(R.id.txt_apellidos)
        val txtFechaNac = findViewById<EditText>(R.id.txt_fecha_nac)
        val txtEmail = findViewById<EditText>(R.id.txt_email_reg)
        val txtPass = findViewById<EditText>(R.id.txt_pass_reg)
        val txtPassConfirm = findViewById<EditText>(R.id.txt_pass_confirm)

        // Botón para regresar
        btnBack.setOnClickListener {
            finish()
        }

        // Configuración del Calendario para Fecha de Nacimiento
        txtFechaNac.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Formato DD/MM/AAAA
                val date = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
                txtFechaNac.setText(date)
            }, year, month, day)

            dpd.show()
        }

        btnRegistrar.setOnClickListener {
            val nombres = txtNombres.text.toString()
            val apellidos = txtApellidos.text.toString()
            val fecha = txtFechaNac.text.toString()
            val email = txtEmail.text.toString()
            val pass = txtPass.text.toString()
            val passConfirm = txtPassConfirm.text.toString()

            if (nombres.isEmpty() || apellidos.isEmpty() || fecha.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            } else if (pass != passConfirm) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Usuario creado exitosamente", Toast.LENGTH_LONG).show()
                // Regresar al login (MainActivity) automáticamente
                finish()
            }
        }
    }
}
