package osacar.rauda.crudoscarb2

import Modelo.Conexion
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtCorreoR = findViewById<EditText>(R.id.txtCorreoREG)
        val txtContrasenaR = findViewById<EditText>(R.id.txtContrasenaREG)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)
        val brnIrALogin = findViewById<Button>(R.id.btnIrALogin)

        brnIrALogin.setOnClickListener {
            val pantallaLogin = Intent(this, Login::class.java)
            startActivity(pantallaLogin)
        }

        btnRegistrarse.setOnClickListener{

                if (txtCorreoR.text.isEmpty() || txtContrasenaR.text.isEmpty()) {
                    Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                    txtCorreoR.setText("")
                    txtContrasenaR.setText("")
                } else { CoroutineScope(Dispatchers.IO).launch {

                    try {

                        val objConexion = Conexion().cadenaConexion()

                        if (objConexion != null) {
                            val addUsuario = objConexion.prepareStatement("insert into Regitro (UUID_Regitro, Correo, contrasena) values(?, ?, ?)")
                            addUsuario.setString(1, UUID.randomUUID().toString())
                            addUsuario.setString(2, txtCorreoR.text.toString())
                            addUsuario.setString(3, txtContrasenaR.text.toString())
                            addUsuario.executeUpdate()

                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@MainActivity, "Usuario creado", Toast.LENGTH_SHORT).show()
                                txtCorreoR.setText("")
                                txtContrasenaR.setText("")

                                // Redirigir a la actividad de login
                                val pantallaLogin = Intent(this@MainActivity, Login::class.java)
                                startActivity(pantallaLogin)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@MainActivity,
                                "Error: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
            }
        }
    }

}