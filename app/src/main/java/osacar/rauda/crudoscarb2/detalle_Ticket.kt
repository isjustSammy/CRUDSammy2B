package osacar.rauda.crudoscarb2

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class detalle_Ticket : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_ticket)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Recibir los valores
        val UUIDRecibido = intent.getStringExtra("UUID_Ticket")
        val num_ticketRecibido = intent.getIntExtra("Numero_Ticket", 0)
        val tituloRecibido = intent.getStringExtra("Titulo_Ticket")
        val descripcionRecibido = intent.getStringExtra("Descripcion_Ticket")
        val autorRecibida = intent.getStringExtra("Autor_Ticket")
        val email_autorRecibida = intent.getStringExtra("Correo_autor")
        val fecha_ticketRecibida = intent.getStringExtra("Fecha")
        val estadoRecibida = intent.getStringExtra("Estado_Ticket")
        val fecha_fin_ticketRecibida = intent.getStringExtra("FechaF")


        val txtUUIDetalle = findViewById<TextView>(R.id.txtUUIDetalle)
        val txtNumTicketDet = findViewById<TextView>(R.id.txtNumTicketDet)
        val txtEstadoDet = findViewById<TextView>(R.id.txtEstadoDet)
        val txtFechaInicioDet = findViewById<TextView>(R.id.txtFechaInicioDet)
        val txtFechaFinalDet = findViewById<TextView>(R.id.txtFechaFinalDet)
        val txtTituloDet = findViewById<TextView>(R.id.txtTituloDet)
        val txtDescripcionDet = findViewById<TextView>(R.id.txtDescripcionDet)
        val txtAutorDet = findViewById<TextView>(R.id.txtAutorDet)
        val txtEmailAutorDet = findViewById<TextView>(R.id.txtEmailAutorDet)


        txtUUIDetalle.text = UUIDRecibido
        txtNumTicketDet.text = num_ticketRecibido.toString()
        txtEstadoDet.text = estadoRecibida
        txtFechaInicioDet.text = fecha_ticketRecibida
        txtFechaFinalDet.text = fecha_ticketRecibida
        txtTituloDet.text = tituloRecibido
        txtDescripcionDet.text = descripcionRecibido
        txtAutorDet.text = autorRecibida
        txtEmailAutorDet.text = email_autorRecibida



    }
}