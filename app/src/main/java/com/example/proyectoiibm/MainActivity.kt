package com.example.proyectoiibm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyectoiibm.ui.theme.ProyectoIIBMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        /* R de recursos, llamar un layout o cualquier recurso del proyecto*/
        setContentView(R.layout.activity_main)

        //Recupera valores..
        val mail = findViewById<EditText>(R.id.txt_mail)
        //val phone = findViewById<EditText>(R.id.txt_phone)
        val login = findViewById<Button>(R.id.btn_login)

        //Evento para el botón...
//        login.setOnClickListener {
//            Log.i("MainActivity","Estoy dando clic al botón")
//            //log.d("MainActivity","Estoy dando clic al botón")
//            val msg = "Email: ${mail.text} Phone: ${phone.text})"
//            Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
//        }

        login.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            intent.putExtra("clave",mail.text.toString())
            startActivity(intent)
        }
    }
}
