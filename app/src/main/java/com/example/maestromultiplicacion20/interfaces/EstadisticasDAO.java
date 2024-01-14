package com.example.maestromultiplicacion20.interfaces;


import com.example.maestromultiplicacion20.modelo.Estadisticas;
import com.example.maestromultiplicacion20.modelo.Usuario;

import java.util.List;

/**
 * Interfaz DAO para acceder a la base de datos
 */
public interface EstadisticasDAO {
    /**
     * Método para obtener todas la fechas que tiene en la base de datos
     * @param usuario nombre de usuario para obtener todas las fechas que tiene ese usuario
     * @return devuelve una lista de String con todas las fechas de ese usuario
     */
    public List<String> obtenerFechas(String usuario);

    /**
     * Método para obtener todas las estadisticas de ese usuario y de esa fecha pasada por paramentros
     * @param fecha
     * @param usuario
     * @return devuelve una lista de Estadisticas de ese usuario con esas fechas.
     */
    public List<Estadisticas> obtenerEstadisticas(String fecha, String usuario);

    /**
     * Método paras insetar nuevas estadísticas a la base de datos
     * @param porcentaje porcentaje de exito de esa tabla
     * @param tabla tabla realizada
     * @param tablasFallidas lista de todos los fallo producidos en esa tabla
     * @param idUsuario el id de ese usuario para relazionarlo en la tabla de Usuario
     * @param avatarJugado la referencia del avatar que ha intentado conseguir o lo ha conseguido
     * @return true si se ha insertado con exito, false si no se ha insertado con exito
     */
    public boolean insertarEstadisticas(String porcentaje, String tabla, List<String> tablasFallidas, int idUsuario, int avatarJugado);

    /**
     * Método para obtener todos los usuarios de la base de datos
     * @return devuelve una lista de Usuarios
     */
    public List<Usuario> obtenerUsuarios();

    /**
     * Método para obtener el id del nombre de ese usuario pasado por paramentros
     * @param nombreUsuario nombre de usuario
     * @return devuelve un Integer con el id de ese usuario en la base de datos
     */
    public int obtenerIdUsuario(String nombreUsuario);

    /**
     * Método para registrar un nuevo usuario en la base de datos
     * @param usuario nombre del usuario
     * @param contrasenia constraseña si es administrado, si no es administrado no es necesario
     * @param tipoCuenta el tipo de cuenta, ya se Administrador o Usuario
     * @param avatar referencia del avatar que se mostrar en el inicio de la aplicación
     * @return devuelve una cadena de String si esta logeado o no
     */
    public String registrarUsuario(String usuario, String contrasenia, String tipoCuenta, int avatar);

    /**
     * Método para obtener el usuario que se quiere logear a la aplicación (debe de ser Admnistrador)
     * @param nombre nombre del usuario
     * @return devuelve un Usuario
     */
    public Usuario obtenerUsuario(String nombre);

    /**
     * Método para obtener todas las estadisticas de ese usuario, con fechas repetidas.
     * @param nombreUsuario nombre de ese usuario
     * @return devuelve un lista de todas las estadisticas
     */
    public List<Estadisticas> obtenerEstadisticasUsuario(String nombreUsuario);

    /**
     * Método para eliminar todas las estadiscas de la base de datos
     */
    public void eliminarEstadisticas();

    /**
     * Método que elimina un Usuario por su nombre en la base de datos
     * @param nombre
     */
    public void eliminarUsuarios(String nombre);
}
