package osacar.rauda.crudoscarb2

import Modelo.Conexion
import Modelo.Ticket
import ReciclerViewHelpers.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.sql.Date
import java.text.ParseException
import java.util.UUID

class Tickets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tickets)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtNumTicket = findViewById<EditText>(R.id.txtNumTicket)
        val txtEstado = findViewById<EditText>(R.id.txtEstado)
        val txtFechaInicio = findViewById<EditText>(R.id.txtFechaInicio)
        val txtFechaFinal = findViewById<EditText>(R.id.txtFechaFinal)
        val txtTitulo = findViewById<EditText>(R.id.txtTitulo)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcion)
        val txtAutor = findViewById<EditText>(R.id.txtAutor)
        val txtEmailAutor = findViewById<EditText>(R.id.txtEmailAutor)
        val rcvTicket = findViewById<RecyclerView>(R.id.rcvTicket)

        rcvTicket.layoutManager = LinearLayoutManager(this)

        fun obtenerTickets(): List<Ticket>{

            val objConexion = Conexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from Ticket")!!

            val ListaTicket = mutableListOf<Ticket>()

            while (resultSet.next()){

                val UUID_TICKET = resultSet.getString("UUID_Ticket")
                val num_ticket = resultSet.getInt("Numero_Ticket")
                val titulo = resultSet.getString("Titulo_Ticket")
                val descripcion = resultSet.getString("Descripcion_Ticket")
                val autor = resultSet.getString("Autor_Ticket")
                val email_autor = resultSet.getString("Correo_autor")
                val fecha_ticket = resultSet.getString("Fecha")
                val estado = resultSet.getString("Estado_Ticket")
                val fecha_fin_ticket = resultSet.getString("FechaF")

                val valoresJuntos = Ticket(UUID_TICKET, num_ticket, titulo, descripcion, autor, email_autor, fecha_ticket, estado, fecha_fin_ticket)

                ListaTicket.add(valoresJuntos)
            }
            return ListaTicket

        }

        CoroutineScope(Dispatchers.IO).launch {
            val TicketDB = obtenerTickets()
            withContext(Dispatchers.Main){
                val adapter = Adaptador(TicketDB)
                rcvTicket.adapter = adapter
            }
        }

        val btnTicket = findViewById<Button>(R.id.btnTicket)

        btnTicket.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = Conexion().cadenaConexion()

                try {


                    // Validación de campos vacíos
                    if (txtNumTicket.text.isEmpty() || txtTitulo.text.isEmpty() || txtDescripcion.text.isEmpty() ||
                        txtAutor.text.isEmpty() || txtEmailAutor.text.isEmpty() || txtFechaInicio.text.isEmpty() ||
                        txtFechaFinal.text.isEmpty()
                    ) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@Tickets,
                                "Por favor, completa todos los campos.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@launch
                    }


                    val addTicket =
                        objConexion?.prepareStatement("insert into Ticket (UUID_Ticket, Numero_Ticket, Titulo_Ticket, Descripcion_Ticket, Autor_Ticket, Correo_autor, Fecha, Estado_Ticket, FechaF) values (?,?,?,?,?,?,?,?,?)")!!

                    addTicket.setString(1, UUID.randomUUID().toString())
                    addTicket.setInt(2, txtNumTicket.text.toString().toInt())
                    addTicket.setString(3, txtTitulo.text.toString())
                    addTicket.setString(4, txtDescripcion.text.toString())
                    addTicket.setString(5, txtAutor.text.toString())
                    addTicket.setString(6, txtEmailAutor.text.toString())
                    addTicket.setString(7, txtEstado.text.toString())
                    addTicket.setString(8, txtFechaInicio.text.toString())
                    addTicket.setString(9, txtFechaFinal.text.toString())

                    addTicket.executeUpdate()

                    //Refresco la lista
                    val nuevasMascotas = obtenerTickets()
                    withContext(Dispatchers.Main) {
                        (rcvTicket.adapter as? Adaptador)?.actualizarLista(nuevasMascotas)
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Tickets, "Ticket creado", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Tickets, "Error: ${e.message}", Toast.LENGTH_LONG)
                            .show()
                        println("Error: ${e.message}")
                        e.printStackTrace()
                    }

                }
            }


        }
    }
}

