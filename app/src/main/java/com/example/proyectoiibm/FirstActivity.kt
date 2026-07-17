package com.example.proyectoiibm

import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.proyectoiibm.data.local.SanaYaDatabase
import com.example.proyectoiibm.data.local.entity.UserEntity
import com.example.proyectoiibm.data.repository.SanaYaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        val btnRegistrar = findViewById<Button>(R.id.btn_finalizar_registro)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)

        // Botón para regresar
        btnBack?.setOnClickListener {
            finish()
        }
        
        val txtNombres = findViewById<EditText>(R.id.txt_nombres)
        val txtApellidos = findViewById<EditText>(R.id.txt_apellidos)
        val txtFechaNac = findViewById<EditText>(R.id.txt_fecha_nac)
        val txtEmail = findViewById<EditText>(R.id.txt_email_reg)
        val txtPass = findViewById<EditText>(R.id.txt_pass_reg)
        val txtPassConfirm = findViewById<EditText>(R.id.txt_pass_confirm)



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
            val nombres = txtNombres.text.toString().trim()
            val apellidos = txtApellidos.text.toString().trim()
            val fecha = txtFechaNac.text.toString().trim()
            val email = txtEmail.text.toString().trim()
            val pass = txtPass.text.toString().trim()
            val passConfirm = txtPassConfirm.text.toString().trim()

            var hasError = false

            if (nombres.isEmpty()) {
                txtNombres.error = "Campo obligatorio"
                hasError = true
            }
            if (apellidos.isEmpty()) {
                txtApellidos.error = "Campo obligatorio"
                hasError = true
            }
            if (fecha.isEmpty()) {
                txtFechaNac.error = "Campo obligatorio"
                hasError = true
            }
            if (email.isEmpty()) {
                txtEmail.error = "Campo obligatorio"
                hasError = true
            }
            if (pass.isEmpty()) {
                txtPass.error = "Campo obligatorio"
                hasError = true
            } else if (pass.length < 8) {
                txtPass.error = "La contraseña debe tener al menos 8 caracteres"
                hasError = true
            }

            if (hasError) {
                Toast.makeText(this, "Por favor complete correctamente todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass != passConfirm) {
                txtPassConfirm.error = "Las contraseñas no coinciden"
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Registro en Base de Datos (SanaYa)
            val nuovoUsuario = UserEntity(
                names = nombres,
                lastName = apellidos,
                birthDate = fecha,
                email = email,
                password = pass
            )

            lifecycleScope.launch(Dispatchers.IO) {
                val db = SanaYaDatabase.getDatabase(this@FirstActivity)
                val repository = SanaYaRepository(db.userDao(), db.specialtyDao(), db.doctorDao(), db.appointmentDao())
                
                val result = repository.registrarUsuario(nuovoUsuario)
                
                withContext(Dispatchers.Main) {
                    result.onSuccess {
                        Toast.makeText(this@FirstActivity, "Usuario creado exitosamente", Toast.LENGTH_LONG).show()
                        finish()
                    }.onFailure {
                        txtEmail.error = it.message
                        Toast.makeText(this@FirstActivity, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
