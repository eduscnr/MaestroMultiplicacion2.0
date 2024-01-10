package com.example.maestromultiplicacion20.servicios;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.maestromultiplicacion20.MainActivity;
import com.example.maestromultiplicacion20.database.EstadisticasDAOImpl;
import com.example.maestromultiplicacion20.inicio.MainActivityPrincipal;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.example.maestromultiplicacion20.modelo.Usuario;

import java.util.List;

public class MyService extends Service {
    private static boolean enviarEstadisticas = false;
    private static List<String> multiplicacionesFallidas;
    private static int porcentajeExito;
    private static int tablaSeleccionada;
    private static String usuarioLogeado;
    private static int avatarJugado;
    /**
     * Método para deneter el servicio cuando la aplicación ha sido cerrada en las tareas de segundo plano
     * @param rootIntent
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        System.out.println("Servicio Detenido");
        if(!MainActivityPrincipal.isEnviarEstadisticas()){
            EstadisticasDAO estadisticasDAO = new EstadisticasDAOImpl(this);
            estadisticasDAO.insertarEstadisticas(String.valueOf(porcentajeExito), String.valueOf(tablaSeleccionada), multiplicacionesFallidas,
                    estadisticasDAO.obtenerIdUsuario(usuarioLogeado), avatarJugado);
            System.out.println("Estadísticas enviadas");
        }
        stopSelf();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        multiplicacionesFallidas = intent.getStringArrayListExtra("multiplicacionesFallidas");
        porcentajeExito = intent.getIntExtra("porcentajeExito", 0);
        tablaSeleccionada = intent.getIntExtra("tablaSeleccionada", 0);
        usuarioLogeado = intent.getStringExtra("usuarioLogeado");
        avatarJugado = intent.getIntExtra("avatarJugado", 0);
        System.out.println("Comprobar Valores: ");
        System.out.println(porcentajeExito);
        System.out.println(usuarioLogeado);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}