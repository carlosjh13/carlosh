package com.example.heredia

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class registrarse : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrer)

        val txtnombre_nuevo: TextView = findViewById(R.id.txtNombre)
        val txtemail_nuevo: TextView = findViewById(R.id.edtCorreoNuevo)
        val txtpassword_nuevo: TextView = findViewById(R.id.edtContraseñaNueva)
        val txtconfirmarpassword_nuevo: TextView = findViewById(R.id.edtConfirmarContraseñaNuevo)
        val btnregistarsenuevo: TextView = findViewById(R.id.btnRegistrarseNuevo)
        val passwordStrengthProgressBar: ProgressBar = findViewById(R.id.passwordStrengthProgressBar)
        val passwordStrengthText: TextView = findViewById(R.id.passwordStrengthText)

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]{3,}"

        btnregistarsenuevo.setOnClickListener() {
            var pass1 = txtpassword_nuevo.text.toString()
            var pass2 = txtconfirmarpassword_nuevo.text.toString()
            val email = txtemail_nuevo.text.toString()
            val nombre = txtnombre_nuevo.text.toString()

            if (nombre.isEmpty()) {
                Toast.makeText(baseContext, "Por favor, introduce tu nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.matches(emailPattern.toRegex())) {
                Toast.makeText(baseContext, "Por favor, introduce un correo electrónico válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass1.isEmpty() || pass2.isEmpty()) {
                Toast.makeText(baseContext, "Por favor, introduce una contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass1 == pass2) {
                createAccount(email, pass1, nombre)
            } else {
                Toast.makeText(baseContext, "Las contraseñas no coinciden. Por favor, inténtalo de nuevo.", Toast.LENGTH_SHORT).show()
                txtconfirmarpassword_nuevo.requestFocus()
            }
        }

        txtpassword_nuevo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updatePasswordStrength(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        txtnombre_nuevo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                txtemail_nuevo.requestFocus()
                true
            } else {
                false
            }
        }

        txtemail_nuevo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                txtpassword_nuevo.requestFocus()
                true
            } else {
                false
            }
        }

        txtpassword_nuevo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                txtconfirmarpassword_nuevo.requestFocus()
                true
            } else {
                false
            }
        }

        txtconfirmarpassword_nuevo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Aquí puedes llamar a tu function de registro
                true
            } else {
                false
            }
        }

        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun createAccount(email: String, password: String, nombre: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(baseContext, "Registrado con Éxito", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Para cerrar la actividad actual y evitar que el usuario regrese presionando el botón de retroceso
            } else {
                Toast.makeText(baseContext, "Ha ocurrido un error. Por favor, revisa los datos e inténtalo de nuevo." + task.exception, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePasswordStrength(password: String) {
        var strength = 0

        if (password.length >= 8) {
            strength++
        }
        if (password.any { it.isDigit() }) {
            strength++
        }
        if (password.any { it.isUpperCase() }) {
            strength++
        }
        if (password.any { it.isLowerCase() }) {
            strength++
        }
        if (password.contains(Regex("[^a-zA-Z0-9]"))) {
            strength++
        }

        findViewById<ProgressBar>(R.id.passwordStrengthProgressBar).progress = strength

        val strengthColor = when (strength) {
            in 0..1 -> ContextCompat.getColor(this, R.color.colorWeak)
            in 2..4 -> ContextCompat.getColor(this, R.color.colorMedium)
            5 -> ContextCompat.getColor(this, R.color.colorStrong)
            else -> ContextCompat.getColor(this, R.color.colorWeak)
        }

        findViewById<ProgressBar>(R.id.passwordStrengthProgressBar).progressDrawable.setTint(strengthColor)
        findViewById<TextView>(R.id.passwordStrengthText).text = when (strength) {
            in 0..1 -> "Contraseña Débil"
            in 2..4 -> "Contraseña Media"
            5 -> "Contraseña Fuerte"
            else -> "Contraseña Débil"
        }
        findViewById<TextView>(R.id.passwordStrengthText).setTextColor(strengthColor)
    }
}
