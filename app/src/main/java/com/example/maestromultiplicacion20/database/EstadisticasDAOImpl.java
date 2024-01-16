package com.example.maestromultiplicacion20.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.maestromultiplicacion20.MainActivity;
import com.example.maestromultiplicacion20.inicio.MainActivityPrincipal;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.example.maestromultiplicacion20.modelo.Estadisticas;
import com.example.maestromultiplicacion20.modelo.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Clase para implementar la interfaz DAO que tiene la base de datos.
 */
public class EstadisticasDAOImpl implements EstadisticasDAO {
    private Sqlite dbHelper;
    private SQLiteDatabase db;
    public EstadisticasDAOImpl(Context context) {
        dbHelper = new Sqlite(context);
    }
    @Override
    public List<String> obtenerFechas(String usuario) {
        List<String> fechas = new ArrayList<>();
        String selectFecha = "SELECT fecha from ESTADISTICAS e inner join USUARIO u on e.id_usuario = u.id_usuario " +
                "where u.nombreUsu = ?";
        db = dbHelper.getReadableDatabase();
        Cursor cursorFecha;
        cursorFecha = db.rawQuery(selectFecha, new String[]{usuario});
        if(cursorFecha.moveToFirst()){
            do{
                if (!fechas.contains(cursorFecha.getString(0))){
                    fechas.add(cursorFecha.getString(0));
                }
            }while (cursorFecha.moveToNext());
        }
        cursorFecha.close();
        return fechas;
    }

    @Override
    public List<Estadisticas> obtenerEstadisticas(String fecha, String usuario) {
        List<Estadisticas> estadisticas = new ArrayList<>();
        String selectEstaFech = "SELECT porcentaje, tabla, tablas_fallidas, fecha, avatarJugado " +
                "FROM ESTADISTICAS es inner join USUARIO u on es.id_usuario  = u.id_usuario " +
                "where es.fecha = ? and u.nombreUsu = ?";
        db = dbHelper.getReadableDatabase();
        Cursor cursorEstadisticas = db.rawQuery(selectEstaFech, new String[]{fecha, usuario});
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(cursorEstadisticas.moveToFirst()){
            do{
                String porcetaje = cursorEstadisticas.getString(0);
                int tabla = cursorEstadisticas.getInt(1);
                String tablasFallidasS = cursorEstadisticas.getString(2);
                String fechaS = cursorEstadisticas.getString(3);
                int avatarJugado = cursorEstadisticas.getInt(4);
                GregorianCalendar fechaG = new GregorianCalendar();
                try {
                    fechaG.setTime(dateFormat.parse(fechaS));
                } catch (ParseException e) {
                    System.out.println("Error en parsear la fecha");
                }
                String trozos[];
                List<String> tablasFallidas = new ArrayList<>();
                if(tablasFallidasS != null){
                    if(tablasFallidasS.contains(",")){
                        trozos = tablasFallidasS.split(",");
                        for (String multiplicacion : trozos){
                            tablasFallidas.add(multiplicacion.trim());
                        }
                    }else{
                        tablasFallidas.add(tablasFallidasS);
                    }
                }
                estadisticas.add(new Estadisticas(tabla, porcetaje, tablasFallidas, fechaG, avatarJugado));
            }while (cursorEstadisticas.moveToNext());
        }
        cursorEstadisticas.close();
        return estadisticas;
    }

    @Override
    public boolean insertarEstadisticas(String porcentaje, String tabla, List<String> tablasFallidas, int idUsuario, int avatarJuagado) {
        String multiplicacionFallidaS = "";
        if(tablasFallidas != null &&!tablasFallidas.isEmpty()){
            if(tablasFallidas.size() > 1){
                for (int i = 0; i<tablasFallidas.size();i++){
                    if(i == 0){
                        multiplicacionFallidaS+= tablasFallidas.get(i);
                    }else{
                        multiplicacionFallidaS += ", " + tablasFallidas.get(i);
                    }
                }
                System.out.println(multiplicacionFallidaS);
            }else{
                multiplicacionFallidaS = tablasFallidas.get(0);
            }
        }

        if(MainActivityPrincipal.isEnviarEstadisticas() == false) {
            db = dbHelper.getReadableDatabase();
            db.execSQL("INSERT INTO ESTADISTICAS (porcentaje, tabla, tablas_fallidas, fecha, avatarJugado, id_usuario) VALUES (" +
                    porcentaje + ", " + tabla + ", '" + multiplicacionFallidaS + "', '" + MainActivity.convertirFeche(new GregorianCalendar()) + "', " + avatarJuagado + ", " + idUsuario + ")");
            db.close();
            System.out.println("Inserción realizada");
        }
        return false;
    }


    @Override
    public List<Usuario> obtenerUsuarios() {
        List<Usuario> devolver = new ArrayList<>();
        String selectUsuario = "SELECT nombreUsu, tipoCuenta, avatar, contraseña from USUARIO";
        db = dbHelper.getReadableDatabase();
        Cursor cursorUsuario;
        cursorUsuario = db.rawQuery(selectUsuario, null);
        if(cursorUsuario.moveToFirst()){
            do{
                    devolver.add(new Usuario(cursorUsuario.getString(0), cursorUsuario.getString(1), cursorUsuario.getInt(2), cursorUsuario.getString(3)));
            }while (cursorUsuario.moveToNext());
        }
        cursorUsuario.close();
        return devolver;
    }

    @Override
    public int obtenerIdUsuario(String nombreUsuario) {
        int idUsu = 0;
        String selectUsuario = "Select id_usuario from USUARIO where nombreUsu = ?";
        db = dbHelper.getReadableDatabase();
        Cursor cursorUsuario;
        cursorUsuario = db.rawQuery(selectUsuario, new String[]{nombreUsuario});
        if(cursorUsuario.moveToFirst()){
            do{
                idUsu = cursorUsuario.getInt(0);
            }while (cursorUsuario.moveToNext());
        }
        cursorUsuario.close();
        return idUsu;
    }

    /**
     * Devolver un String para mostrar un texto
     * @param usuario
     * @param contrasenia
     * @param tipoCuenta
     */
    @Override
    public String registrarUsuario(String usuario, String contrasenia, String tipoCuenta, int avatar) {
        db = dbHelper.getReadableDatabase();
        if(contrasenia != null){
            try{
                db.execSQL("INSERT INTO USUARIO (nombreUsu, tipoCuenta, avatar, contraseña) VALUES ('" +
                        usuario + "', '" + tipoCuenta + "', '" + avatar + "', '" + contrasenia + "')");
            }catch (Exception e){
                System.out.println("Usuario ya registrado");
                return "Usuario ya registrado";
            }finally {
                db.close();
            }
        }else{
            try{
                db.execSQL("INSERT INTO USUARIO (nombreUsu, tipoCuenta, avatar) VALUES ('" +
                        usuario + "', '" + tipoCuenta + "', '" + avatar + "')");
            }catch (Exception e){
                System.out.println("Usuario ya registrado");
                return "Usuario ya registrado";
            }finally {
                db.close();
            }
        }
        return "Registrado";
    }

    @Override
    public Usuario obtenerUsuario(String nombre) {
        db = dbHelper.getReadableDatabase();
        Usuario usuario = null;
        String selectUsuario = "SELECT nombreUsu, tipoCuenta, avatar, contraseña from USUARIO where nombreUsu = ?";
        Cursor cursorUsuario = db.rawQuery(selectUsuario, new String[]{nombre});
        if(cursorUsuario.moveToFirst()){
            do{
                usuario = new Usuario(cursorUsuario.getString(0), cursorUsuario.getString(1), cursorUsuario.getInt(2), cursorUsuario.getString(3));
            }while (cursorUsuario.moveToNext());
        }
        return usuario;
    }

    @Override
    public List<Estadisticas> obtenerEstadisticasUsuario(String nombreUsuario){
        String selectEstadisticas = "SELECT porcentaje, tabla, tablas_fallidas, fecha " +
                "FROM ESTADISTICAS es inner join USUARIO u on es.id_usuario  = u.id_usuario " +
                "where u.nombreUsu = ?";
        db = dbHelper.getReadableDatabase();
        Cursor cursorEs = db.rawQuery(selectEstadisticas, new String[]{nombreUsuario});
        List<Estadisticas> estadisticas = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(cursorEs.moveToFirst()){
            do{
                int porcentaje = cursorEs.getInt(0);
                int tabla = cursorEs.getInt(1);
                String tablasFallidasS = cursorEs.getString(2);
                String fecha = cursorEs.getString(3);
                GregorianCalendar gc = new GregorianCalendar();
                try {
                    gc.setTime(dateFormat.parse(fecha));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                String trozos[];
                List<String> tablasFallidas = new ArrayList<>();
                if(tablasFallidasS != null){
                    if(tablasFallidasS.contains(",")){
                        trozos = tablasFallidasS.split(",");
                        for (String multiplicacion : trozos){
                            tablasFallidas.add(multiplicacion);
                        }
                    }else{
                        tablasFallidas.add(tablasFallidasS);
                    }
                }
                estadisticas.add(new Estadisticas(tabla, String.valueOf(porcentaje), tablasFallidas, gc));
            }while (cursorEs.moveToNext());
        }
        return estadisticas;
    }

    @Override
    public void eliminarEstadisticas() {
        db = dbHelper.getWritableDatabase();
        db.delete("ESTADISTICAS", null, null);
        db.close();
    }

    @Override
    public void eliminarUsuarios(String nombre) {
        Cursor cursor = db.rawQuery("SELECT id_usuario FROM USUARIO WHERE nombreUsu = ?", new String[]{nombre});

        if (cursor.moveToFirst()) {
            int idUsuario = cursor.getInt(0);

            db.execSQL("DELETE FROM USUARIO WHERE nombreUsu = '" + nombre + "'");
            db.execSQL("DELETE FROM ESTADISTICAS WHERE id_usuario = " + idUsuario);

        }

        cursor.close();
    }
}
