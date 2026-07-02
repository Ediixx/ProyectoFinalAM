package com.example.proyectoiibm

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

data class Doctor(val nombre: String, val disponible: Boolean)

class AgendarCitaActivity : AppCompatActivity() {

    private var pasoActual = 0
    private val totalPasos = 4

    private var especialidadSeleccionada: String? = null
    private var doctorSeleccionado: String? = null
    private var fechaSeleccionada: String? = null
    private var horaSeleccionada: String? = null

    private val especialidades = listOf("Cardiología", "Neurología", "Odontología", "Oftalmología")

    private val doctoresPorEspecialidad = mapOf(
        "Cardiología" to listOf(
            Doctor("Dr. Carlos López", true),
            Doctor("Dra. Patricia Ruiz", true),
            Doctor("Dr. Manuel Torres", false)
        ),
        "Neurología" to listOf(
            Doctor("Dra. Ana Vega", true),
            Doctor("Dr. Jorge Salas", false)
        ),
        "Odontología" to listOf(
            Doctor("Dr. Luis Peña", true),
            Doctor("Dra. Sofía Cruz", true)
        ),
        "Oftalmología" to listOf(
            Doctor("Dra. Elena Ríos", true),
            Doctor("Dr. Mario Ibarra", false)
        )
    )

    private val horarios = listOf(
        "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00"
    )

    // Views
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var progressFill: View
    private lateinit var txtEstadoProgreso: TextView
    private lateinit var btnAtras: Button
    private lateinit var btnSiguiente: Button
    private lateinit var gridEspecialidades: GridLayout
    private lateinit var listaDoctores: LinearLayout
    private lateinit var txtEspecialidadSeleccionada: TextView
    private lateinit var resumenPaso3: LinearLayout
    private lateinit var txtFechaCita: EditText
    private lateinit var btnCalendario: Button
    private lateinit var resumenPaso4: LinearLayout
    private lateinit var gridHorarios: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendar_cita)

        viewFlipper = findViewById(R.id.view_flipper)
        progressFill = findViewById(R.id.progress_fill)
        txtEstadoProgreso = findViewById(R.id.txt_estado_progreso)
        btnAtras = findViewById(R.id.btn_atras_agendar)
        btnSiguiente = findViewById(R.id.btn_siguiente_agendar)
        gridEspecialidades = findViewById(R.id.grid_especialidades)
        listaDoctores = findViewById(R.id.lista_doctores)
        txtEspecialidadSeleccionada = findViewById(R.id.txt_especialidad_seleccionada)
        resumenPaso3 = findViewById(R.id.resumen_paso3)
        txtFechaCita = findViewById(R.id.txt_fecha_cita)
        btnCalendario = findViewById(R.id.btn_calendario)
        resumenPaso4 = findViewById(R.id.resumen_paso4)
        gridHorarios = findViewById(R.id.grid_horarios)

        findViewById<ImageButton>(R.id.btn_back_agendar).setOnClickListener { finish() }

        construirEspecialidades()

        txtFechaCita.setOnClickListener { mostrarDatePicker() }
        btnCalendario.setOnClickListener { mostrarDatePicker() }

        btnAtras.setOnClickListener {
            if (pasoActual == 0) finish() else {
                pasoActual--
                actualizarUI()
            }
        }

        btnSiguiente.setOnClickListener {
            if (pasoActual == totalPasos - 1) {
                mostrarDialogoExito()
            } else {
                pasoActual++
                actualizarUI()
            }
        }

        actualizarUI()
    }

    private fun construirEspecialidades() {
        gridEspecialidades.removeAllViews()
        val iconos = mapOf(
            "Cardiología" to "❤️", "Neurología" to "🧠",
            "Odontología" to "🦷", "Oftalmología" to "👁️"
        )
        especialidades.forEach { esp ->
            val card = crearTarjeta(iconos[esp] ?: "", esp)
            card.setOnClickListener {
                especialidadSeleccionada = esp
                doctorSeleccionado = null
                construirEspecialidades()
                btnSiguiente.isEnabled = true
            }
            actualizarEstiloTarjeta(card, esp == especialidadSeleccionada, true)
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8, 8, 8, 8)
            }
            gridEspecialidades.addView(card, params)
        }
    }

    private fun crearTarjeta(icono: String, texto: String): LinearLayout {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(32, 32, 32, 32)

        val txtIcono = TextView(this)
        txtIcono.text = icono
        txtIcono.textSize = 32f
        txtIcono.gravity = Gravity.CENTER
        layout.addView(txtIcono)

        val txtNombre = TextView(this)
        txtNombre.text = texto
        txtNombre.textSize = 16f
        txtNombre.gravity = Gravity.CENTER
        txtNombre.setPadding(0, 16, 0, 0)
        layout.addView(txtNombre)

        return layout
    }

    private fun actualizarEstiloTarjeta(card: LinearLayout, seleccionada: Boolean, disponible: Boolean) {
        card.setBackgroundResource(
            when {
                !disponible -> R.drawable.bg_card_disabled
                seleccionada -> R.drawable.bg_card_selected
                else -> R.drawable.bg_card
            }
        )
    }

    private fun construirDoctores() {
        listaDoctores.removeAllViews()
        val esp = especialidadSeleccionada ?: return
        txtEspecialidadSeleccionada.text = "Especialidad: $esp"

        doctoresPorEspecialidad[esp]?.forEach { doc ->
            val card = LinearLayout(this)
            card.orientation = LinearLayout.VERTICAL
            card.setPadding(24, 24, 24, 24)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 16)
            card.layoutParams = params

            val txtNombre = TextView(this)
            txtNombre.text = doc.nombre
            txtNombre.textSize = 16f
            txtNombre.setTextColor(if (doc.disponible) Color.BLACK else Color.GRAY)
            card.addView(txtNombre)

            val txtEstado = TextView(this)
            txtEstado.text = if (doc.disponible) "✓ Disponible" else "✗ No disponible"
            txtEstado.setTextColor(if (doc.disponible) Color.parseColor("#2ECC71") else Color.GRAY)
            card.addView(txtEstado)

            actualizarEstiloTarjeta(card, doc.nombre == doctorSeleccionado, doc.disponible)

            if (doc.disponible) {
                card.setOnClickListener {
                    doctorSeleccionado = doc.nombre
                    construirDoctores()
                    btnSiguiente.isEnabled = true
                }
            }
            listaDoctores.addView(card)
        }
    }

    private fun construirResumen(contenedor: LinearLayout) {
        contenedor.removeAllViews()
        val items = mutableListOf("📋 $especialidadSeleccionada")
        doctorSeleccionado?.let { items.add("🧑‍⚕️ $it") }
        fechaSeleccionada?.let { items.add("📅 $it") }

        items.forEach {
            val txt = TextView(this)
            txt.text = it
            txt.textSize = 15f
            txt.setPadding(0, 4, 0, 4)
            contenedor.addView(txt)
        }
    }

    private fun mostrarDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            fechaSeleccionada = String.format("%02d/%02d/%d", day, month + 1, year)
            txtFechaCita.setText(fechaSeleccionada)
            btnSiguiente.isEnabled = true
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun construirHorarios() {
        gridHorarios.removeAllViews()
        horarios.forEach { hora ->
            val btn = Button(this)
            btn.text = hora
            btn.textSize = 14f
            btn.setBackgroundResource(if (hora == horaSeleccionada) R.drawable.bg_card_selected else R.drawable.bg_card)
            btn.setTextColor(Color.BLACK)
            btn.setOnClickListener {
                horaSeleccionada = hora
                construirHorarios()
                btnSiguiente.isEnabled = true
                btnSiguiente.text = "Confirmar Cita"
            }
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8, 8, 8, 8)
            }
            gridHorarios.addView(btn, params)
        }
    }

    private fun actualizarUI() {
        viewFlipper.displayedChild = pasoActual

        // Progreso
        val params = progressFill.layoutParams
        val anchoTotal = 100
        params.width = (anchoTotal * (pasoActual + 1) / totalPasos) * 4 // aproximado en dp*density
        progressFill.layoutParams = params

        txtEstadoProgreso.text = if (pasoActual == totalPasos - 1) "✓ Listo para confirmar" else "Completa todos los datos"

        btnAtras.text = if (pasoActual == 0) "Cancelar" else "Atrás"
        btnSiguiente.text = if (pasoActual == totalPasos - 1) "Confirmar Cita" else "Siguiente"

        when (pasoActual) {
            0 -> btnSiguiente.isEnabled = especialidadSeleccionada != null
            1 -> {
                construirDoctores()
                btnSiguiente.isEnabled = doctorSeleccionado != null
            }
            2 -> {
                construirResumen(resumenPaso3)
                txtFechaCita.setText(fechaSeleccionada ?: "")
                btnSiguiente.isEnabled = fechaSeleccionada != null
            }
            3 -> {
                construirResumen(resumenPaso4)
                construirHorarios()
                btnSiguiente.isEnabled = horaSeleccionada != null
            }
        }
    }

    private fun mostrarDialogoExito() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_cita_exito, null)
        val txtResumen = dialogView.findViewById<TextView>(R.id.txt_resumen_dialog)
        txtResumen.text = "$especialidadSeleccionada con $doctorSeleccionado el\n$fechaSeleccionada a las $horaSeleccionada"

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btn_ok_dialog).setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }
}