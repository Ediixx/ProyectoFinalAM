package com.example.proyectoiibm

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyectoiibm.data.local.SanaYaDatabase
import com.example.proyectoiibm.data.local.entity.AppointmentEntity
import com.example.proyectoiibm.data.local.entity.DoctorEntity
import com.example.proyectoiibm.data.local.entity.SpecialtyEntity
import com.example.proyectoiibm.data.repository.SanaYaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class AgendarCitaActivity : AppCompatActivity() {

    private var pasoActual = 0
    private val totalPasos = 4

    private var especialidadSeleccionada: SpecialtyEntity? = null
    private var doctorSeleccionado: DoctorEntity? = null
    private var fechaSeleccionada: String? = null
    private var horaSeleccionada: String? = null

    private var listaEspecialidadesDB: List<SpecialtyEntity> = emptyList()
    private var listaDoctoresDB: List<DoctorEntity> = emptyList()

    private val horarios = listOf(
        "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00"
    )

    // Repository
    private lateinit var repository: SanaYaRepository
    private var userId: Long = -1L

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

        userId = intent.getLongExtra("userId", -1L)

        val db = SanaYaDatabase.getDatabase(this)
        repository = SanaYaRepository(db.userDao(), db.specialtyDao(), db.doctorDao(), db.appointmentDao())

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

        cargarEspecialidades()

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
                confirmarYGuardarCita()
            } else {
                pasoActual++
                actualizarUI()
            }
        }

        actualizarUI()
    }

    private fun cargarEspecialidades() {
        lifecycleScope.launch(Dispatchers.IO) {
            listaEspecialidadesDB = repository.obtenerEspecialidades()
            
            // Si está vacío, esperamos un momento por si el seeding está en proceso (solo en primer arranque)
            if (listaEspecialidadesDB.isEmpty()) {
                kotlinx.coroutines.delay(500)
                listaEspecialidadesDB = repository.obtenerEspecialidades()
            }

            withContext(Dispatchers.Main) {
                construirEspecialidades()
            }
        }
    }

    private fun construirEspecialidades() {
        gridEspecialidades.removeAllViews()
        
        val isLandscape = resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
        gridEspecialidades.columnCount = if (isLandscape) 4 else 2

        val iconosMapping = mapOf(
            "Cardiología" to "❤️", "Neurología" to "🧠",
            "Odontología" to "🦷", "Oftalmología" to "👁️",
            "Médico General" to "👨‍⚕️", "Traumatología" to "🦴",
            "Vacunación" to "💉", "Pediatría" to "👶"
        )
        
        listaEspecialidadesDB.forEach { esp ->
            val emoji = iconosMapping[esp.nombre] ?: "🏥"
            val card = crearTarjeta(emoji, esp.nombre)
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
        txtNombre.textSize = 14f
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

    private fun cargarDoctores() {
        val espId = especialidadSeleccionada?.id ?: return
        lifecycleScope.launch(Dispatchers.IO) {
            listaDoctoresDB = repository.obtenerDoctoresPorEspecialidad(espId)
            withContext(Dispatchers.Main) {
                construirDoctores()
            }
        }
    }

    private fun construirDoctores() {
        listaDoctores.removeAllViews()
        val esp = especialidadSeleccionada ?: return
        txtEspecialidadSeleccionada.text = "Especialidad: ${esp.nombre}"

        listaDoctoresDB.forEach { doc ->
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
            txtNombre.setTextColor(Color.BLACK)
            card.addView(txtNombre)

            val txtDir = TextView(this)
            txtDir.text = doc.direccion
            txtDir.textSize = 12f
            txtDir.setTextColor(Color.GRAY)
            card.addView(txtDir)

            val txtDist = TextView(this)
            txtDist.text = "${doc.distanciaKm} km de distancia"
            txtDist.textSize = 12f
            txtDist.setTextColor(Color.parseColor("#2ECC71"))
            card.addView(txtDist)

            actualizarEstiloTarjeta(card, doc == doctorSeleccionado, true)

            card.setOnClickListener {
                doctorSeleccionado = doc
                construirDoctores()
                btnSiguiente.isEnabled = true
            }
            listaDoctores.addView(card)
        }
    }

    private fun construirResumen(contenedor: LinearLayout) {
        contenedor.removeAllViews()
        val items = mutableListOf("📋 ${especialidadSeleccionada?.nombre ?: ""}")
        doctorSeleccionado?.let { items.add("🧑‍⚕️ ${it.nombre}") }
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
        val isLandscape = resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
        gridHorarios.columnCount = if (isLandscape) 5 else 3

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
        val anchoTotal = resources.displayMetrics.widthPixels - 32 * resources.displayMetrics.density // aprox
        params.width = ((anchoTotal * (pasoActual + 1) / totalPasos)).toInt()
        progressFill.layoutParams = params

        txtEstadoProgreso.text = if (pasoActual == totalPasos - 1) "✓ Listo para confirmar" else "Completa todos los datos"

        btnAtras.text = if (pasoActual == 0) "Cancelar" else "Atrás"
        btnSiguiente.text = if (pasoActual == totalPasos - 1) "Confirmar Cita" else "Siguiente"

        when (pasoActual) {
            0 -> btnSiguiente.isEnabled = especialidadSeleccionada != null
            1 -> {
                cargarDoctores()
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

    private fun confirmarYGuardarCita() {
        val user = userId
        val doc = doctorSeleccionado ?: return
        val esp = especialidadSeleccionada ?: return
        val fecha = fechaSeleccionada ?: ""
        val hora = horaSeleccionada ?: ""

        val cita = AppointmentEntity(
            usuarioId = user,
            doctorId = doc.id,
            especialidadNombre = esp.nombre,
            direccion = doc.direccion,
            fechaTexto = fecha,
            horaTexto = hora,
            fechaHoraMillis = System.currentTimeMillis() // Simplificación
        )

        lifecycleScope.launch(Dispatchers.IO) {
            repository.agendarCita(cita)
            withContext(Dispatchers.Main) {
                mostrarDialogoExito()
            }
        }
    }

    private fun mostrarDialogoExito() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_cita_exito, null)
        val txtResumen = dialogView.findViewById<TextView>(R.id.txt_resumen_dialog)
        txtResumen.text = "${especialidadSeleccionada?.nombre} con ${doctorSeleccionado?.nombre} el\n$fechaSeleccionada a las $horaSeleccionada"

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
