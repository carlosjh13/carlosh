package com.example.heredia


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btningresar: Button = findViewById(R.id.btnIngresar)
        val txtemail: TextView = findViewById(R.id.edtEmail)
        val txtpassword: TextView = findViewById(R.id.edtPassword)
        val btnregistrarse: TextView = findViewById(R.id.btnRegis)
        firebaseAuth= Firebase.auth

        btningresar.setOnClickListener()
        {
            val email = txtemail.text.toString()
            val password = txtpassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(baseContext, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signIn(email, password)
        }

        btnregistrarse.setOnClickListener(){
            val i = Intent(this, registrarse::class.java)
            startActivity(i)
        }
    }
    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
                task ->
            if (task.isSuccessful){
                val user = firebaseAuth.currentUser
                Toast.makeText(baseContext,"Atenticación Exitosa", Toast.LENGTH_SHORT).show()
                //Aqui vamos a ir a la segunda activity
                val i = Intent(this, Iniciarsesion::class.java)
                startActivity(i)
            }
            else{
                Toast.makeText(baseContext,"Error de Correo y/o Contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }

}