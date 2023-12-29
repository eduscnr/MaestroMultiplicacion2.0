package com.example.maestromultiplicacion20.interfaces;


import com.example.maestromultiplicacion20.modelo.Estadisticas;
import com.example.maestromultiplicacion20.modelo.Usuario;

import java.util.List;

public interface EstadisticasDAO {
    public List<String> obtenerFechas(String usuario);
    public List<Estadisticas> obtenerEstadisticas(String fecha, String usuario);
    public boolean insertarEstadisticas(String porcentaje, String tabla, List<String> tablasFallidas, int idUsuario, int avatarJugado);
    public List<Usuario> obtenerUsuarios();
    public int obtenerIdUsuario(String nombreUsuario);
    public String registrarUsuario(String usuario, String contrasenia, String tipoCuenta);
    public Usuario obtenerUsuario(String nombre);
    public List<Estadisticas> obtenerEstadisticasUsuario(String nombreUsuario);
}
