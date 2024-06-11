package Modelo

import java.sql.Connection
import java.sql.DriverManager
import kotlin.Exception

class Conexion {

    fun cadenaConexion(): Connection? {

        try {
            val url = "jdbc:oracle:thin:@192.168.0.22:1521:xe"
                val usuario ="SYSTEM"
                val contrasena = "ITR2024"

            val conexion = DriverManager.getConnection(url, usuario, contrasena)
            return conexion
        }catch (e: Exception){
            println("Este es el error: $e")
            return null
        }
    }

}