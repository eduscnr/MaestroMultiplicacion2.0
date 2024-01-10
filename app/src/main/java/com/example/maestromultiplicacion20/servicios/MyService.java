package com.example.maestromultiplicacion20.servicios;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.maestromultiplicacion20.MainActivity;
import com.example.maestromultiplicacion20.inicio.MainActivityPrincipal;

public class MyService extends Service {
    /**
     * Método para deneter el servicio cuando la aplicación ha sido cerrada en las tareas de segundo plano
     * @param rootIntent
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        System.out.println("Servicio Detenido");
        /*if(!MainActivityPrincipal.isEnviarEstadisticas()){
            System.out.println(MainActivityPrincipal.isEnviarEstadisticas());
            System.out.println("Estadisticas no enviado y ademas servicio denetido");
        }*/
        stopSelf();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}