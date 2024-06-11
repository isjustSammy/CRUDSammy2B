package ReciclerViewHelpers

import Modelo.Conexion
import Modelo.Ticket
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import osacar.rauda.crudoscarb2.R
import osacar.rauda.crudoscarb2.detalle_Ticket

class Adaptador(var Datos: List<Ticket>): RecyclerView.Adapter<ViewHolder>() {

    fun actualizarLista(nuevaLista: List<Ticket>) {
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    fun actualicePantalla(uuid: String, nuevoTitulo: String){
        val index = Datos.indexOfFirst { it.UUID_Ticket == uuid }
        Datos[index].Titulo_Ticket = nuevoTitulo
        notifyDataSetChanged()
    }

    /////////////////// TODO: Eliminar datos
    fun eliminarDatos(titulo: String, posicion: Int){
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){
            val objConexion = Conexion().cadenaConexion()
            val deleteTicket = objConexion?.prepareStatement("delete from Ticket where Titulo_Ticket = ?")!!
            deleteTicket.setString(1, titulo)
            deleteTicket.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()
        }
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    //////////////////////TODO: Editar datos
    fun actualizarDato(nuevoTitulo: String, UUID_TICKET: String){
        GlobalScope.launch(Dispatchers.IO){

            val objConexion = Conexion().cadenaConexion()
            val updateTicket = objConexion?.prepareStatement("update Ticket set Titulo_Ticket = ? where UUID_Ticket = ?")!!
            updateTicket.setString(1, nuevoTitulo)
            updateTicket.setString(2, UUID_TICKET)
            updateTicket.executeUpdate()

            withContext(Dispatchers.Main){
                actualicePantalla(UUID_TICKET, nuevoTitulo)
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)

    }

    override fun getItemCount() = Datos.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = Datos[position]
        holder.txtNombreCard.text = item.Titulo_Ticket

        //todo: clic al icono de eliminar
        holder.imgBorrar.setOnClickListener {

            //Creamos un Alert Dialog
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar el Ticket?")

            //Botones
            builder.setPositiveButton("Si") { dialog, which ->
                eliminarDatos(item.Titulo_Ticket, position)
            }

            builder.setNegativeButton("No"){dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

        }

        //Todo: icono de editar
        holder.imgEditar.setOnClickListener{
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Actualizar")
            builder.setMessage("¿Desea actualizar el Ticket?")
            val cuadroTexto = EditText(context)
            cuadroTexto.setHint(item.Titulo_Ticket)
            builder.setView(cuadroTexto)

            //Botones
            builder.setPositiveButton("Actualizar") { dialog, which ->
                actualizarDato(cuadroTexto.text.toString(), item.UUID_Ticket)
            }

            builder.setNegativeButton("Cancelar"){dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        //Todo: Clic a la card completa

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val pantallaDetalle = Intent(context, detalle_Ticket::class.java)
            pantallaDetalle.putExtra("UUID_TICKET", item.UUID_Ticket)
            pantallaDetalle.putExtra("num_ticket", item.Numero_Ticket)
            pantallaDetalle.putExtra("titulo", item.Titulo_Ticket)
            pantallaDetalle.putExtra("descripcion", item.Descripcion_Ticket)
            pantallaDetalle.putExtra("autor", item.Autor_Ticket)
            pantallaDetalle.putExtra("email_autor", item.Correo_autor)
            pantallaDetalle.putExtra("fecha_ticket", item.Fecha)
            pantallaDetalle.putExtra("estado", item.Estado_Ticket)
            pantallaDetalle.putExtra("fecha_fin_ticket", item.FechaF)
            context.startActivity(pantallaDetalle)
        }

    }


}