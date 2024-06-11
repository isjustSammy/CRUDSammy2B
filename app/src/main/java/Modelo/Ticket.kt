package Modelo

data class Ticket (

    val UUID_Ticket: String,
    val Numero_Ticket: Int,
    var Titulo_Ticket: String,
    val Descripcion_Ticket: String,
    val Autor_Ticket:String,
    val Correo_autor: String,
    val Fecha: String,
    val Estado_Ticket: String,
    val FechaF: String

)