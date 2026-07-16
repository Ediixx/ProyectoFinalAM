package com.example.proyectoiibm

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoiibm.data.local.entity.AppointmentEntity

class CitaAdapter(
    private var citas: List<AppointmentEntity>,
    private val esHistorial: Boolean = false,
    private val onCancelarClick: ((AppointmentEntity) -> Unit)? = null,
    private val onReagendarClick: ((AppointmentEntity) -> Unit)? = null
) : RecyclerView.Adapter<CitaAdapter.CitaViewHolder>() {

    class CitaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtEspecialidad: TextView = view.findViewById(R.id.txt_item_especialidad)
        val txtDoctor: TextView = view.findViewById(R.id.txt_item_doctor)
        val txtFecha: TextView = view.findViewById(R.id.txt_item_fecha)
        val txtHora: TextView = view.findViewById(R.id.txt_item_hora)
        val txtEstado: TextView = view.findViewById(R.id.txt_item_estado)
        val layoutAcciones: LinearLayout = view.findViewById(R.id.layout_acciones)
        val btnCancelar: Button = view.findViewById(R.id.btn_cancelar_cita)
        val btnReagendar: Button = view.findViewById(R.id.btn_reagendar_cita)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cita, parent, false)
        return CitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citas[position]
        holder.txtEspecialidad.text = cita.especialidadNombre
        holder.txtDoctor.text = "Consultorio: ${cita.direccion}"
        holder.txtFecha.text = "📅 ${cita.fechaTexto}"
        holder.txtHora.text = "🕒 ${cita.horaTexto}"
        holder.txtEstado.text = cita.estado
        
        // Estilo según estado
        when(cita.estado) {
            "Agendada" -> {
                holder.txtEstado.setTextColor(Color.parseColor("#2ECC71"))
                holder.layoutAcciones.visibility = if (esHistorial) View.GONE else View.VISIBLE
            }
            "Cancelada" -> {
                holder.txtEstado.setTextColor(Color.RED)
                holder.layoutAcciones.visibility = View.GONE
            }
            else -> {
                holder.txtEstado.setTextColor(Color.GRAY)
                holder.layoutAcciones.visibility = View.GONE
            }
        }

        if (esHistorial) {
            holder.layoutAcciones.visibility = View.GONE
        }

        holder.btnCancelar.setOnClickListener { onCancelarClick?.invoke(cita) }
        holder.btnReagendar.setOnClickListener { onReagendarClick?.invoke(cita) }
    }

    override fun getItemCount() = citas.size

    fun updateCitas(newCitas: List<AppointmentEntity>) {
        citas = newCitas
        notifyDataSetChanged()
    }
}
