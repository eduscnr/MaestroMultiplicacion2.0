package com.example.maestromultiplicacion20.servicios;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.example.maestromultiplicacion20.MainActivity;
import com.example.maestromultiplicacion20.database.EstadisticasDAOImpl;
import com.example.maestromultiplicacion20.inicio.MainActivityPrincipal;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.example.maestromultiplicacion20.modelo.Usuario;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyService extends Service {
    private static boolean enviarEstadisticas = false;
    private static List<String> multiplicacionesFallidas;
    private static int porcentajeExito = 100;
    private static int tablaSeleccionada;
    private static String usuarioLogeado;
    private static int avatarJugado;
    private Set<String> multiplicacionesFallidasSet;
    private SharedPreferences sharedPreferences;
    /**
     * Método para deneter el servicio cuando la aplicación ha sido cerrada en las tareas de segundo plano
     * @param rootIntent
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        multiplicacionesFallidasSet = sharedPreferences.getStringSet("multiplicacionesFallidas", new HashSet<>());
        usuarioLogeado = sharedPreferences.getString("usuarioLogeado", new String(""));
        avatarJugado = sharedPreferences.getInt("avatarJugado", 0);
        enviarEstadisticas = sharedPreferences.getBoolean("enviarEstadisticas", false);
        tablaSeleccionada = sharedPreferences.getInt("tablaSeleccionada", 0);
        if(!enviarEstadisticas){
            System.out.println("Envio estadisiticas porque me ha cerrado la app y no se ha enviado nada");
            System.out.println(tablaSeleccionada);
            System.out.println(usuarioLogeado);
            System.out.println(multiplicacionesFallidasSet);
            System.out.println(porcentajeExito);
            EstadisticasDAO estadisticasDAO = new EstadisticasDAOImpl(this);
            if(multiplicacionesFallidasSet != null){
                multiplicacionesFallidas = new ArrayList<>();
                for (String multiplicaciones : multiplicacionesFallidasSet){
                    multiplicacionesFallidas.add(multiplicaciones);
                    porcentajeExito -= 10;
                }
            }
            estadisticasDAO.insertarEstadisticas(String.valueOf(porcentajeExito), String.valueOf(tablaSeleccionada), multiplicacionesFallidas,
                    estadisticasDAO.obtenerIdUsuario(usuarioLogeado), avatarJugado);
            System.out.println("Estadísticas enviadas");
        }
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("MisPreferencias",  Context.MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Comprobar nullos
        usuarioLogeado = intent.getStringExtra("usuarioLogeado");
        tablaSeleccionada = intent.getIntExtra("tablaSeleccionada", 0);
        System.out.println("recopilo tabla seleccionado servicio: " + tablaSeleccionada);
        multiplicacionesFallidas = intent.getStringArrayListExtra("multiplicacionesFallidas");
        avatarJugado = intent.getIntExtra("avatarJugado",0);
        enviarEstadisticas = intent.getBooleanExtra("enviarEstadisticas", false);
        if(multiplicacionesFallidas != null){
            multiplicacionesFallidasSet = new HashSet<>(multiplicacionesFallidas);
        }
        editor.putInt("tablaSeleccionada", tablaSeleccionada);
        editor.putString("usuarioLogeado", usuarioLogeado);
        editor.putStringSet("multiplicacionesFallidas", multiplicacionesFallidasSet);
        editor.putInt("avatarJugado", avatarJugado);
        editor.putBoolean("enviarEstadisticas", enviarEstadisticas);
        editor.apply();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBind");
        return null;
    }
}