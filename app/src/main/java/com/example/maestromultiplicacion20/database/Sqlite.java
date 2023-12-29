package com.example.maestromultiplicacion20.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.maestromultiplicacion20.R;

public class Sqlite extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DE_DATOS = "EstadisticasDatabases.db";
    private static final int VERSION_BASE_DE_DATOS = 15;
    public Sqlite(Context context) {
        super(context, NOMBRE_BASE_DE_DATOS, null, VERSION_BASE_DE_DATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Crear la tabla Usuario
        sqLiteDatabase.execSQL("CREATE TABLE USUARIO "+ "(" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombreUsu TEXT NOT NULL UNIQUE, " +
                "tipoCuenta TEXT NOT NULL, " +
                "avatar, " +
                "contraseña " + ")");
        // Insertar usuarios de prueba
        insertarUsuario(sqLiteDatabase, "Usuario1", "usuario", R.drawable.icons8_usuario_50, "");
        insertarUsuario(sqLiteDatabase, "Usuario2", "usuario", R.drawable.icons8_usuario_50, "");
        insertarUsuario(sqLiteDatabase, "Mami", "administrador", R.drawable.icons8_usuario_50, "1234");

        // Crear la tabla Estadisticas con clave foránea a la tabla Usuario
        sqLiteDatabase.execSQL("CREATE TABLE ESTADISTICAS " + "(" +
                "id_estadisticas INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "porcentaje INTEGER NOT NULL, " +
                "tabla INTEGER NOT NULL,"+
                "tablas_fallidas TEXT, " +
                "fecha DATE, " +
                "avatarJugado INTEGER, "+
                "id_usuario INTEGER NOT NULL, " +
                "FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)" +
                ")");

        // Insertar datos de ejemplo en las tablas
        insertarEstadistica(sqLiteDatabase, 90, 0, "0x8=8, 0x9=9", "16/12/2023", R.drawable.superman10, 1);
        insertarEstadistica(sqLiteDatabase, 100, 4, "", "16/12/2023", R.drawable.batman10, 1);
        insertarEstadistica(sqLiteDatabase, 100, 8, "", "16/12/2023", R.drawable.spiderman10, 2);
        insertarEstadistica(sqLiteDatabase, 100, 9, "", "17/12/2023", R.drawable.thor10, 1);
        insertarEstadistica(sqLiteDatabase, 100, 10, "", "18/12/2023", R.drawable.ironman10, 2);


        System.out.println("CREADA");
    }

    private void insertarUsuario(SQLiteDatabase db, String nombreUsu, String tipoCuenta, int avatar, String contraseña) {
        db.execSQL("INSERT INTO USUARIO (nombreUsu, tipoCuenta, avatar, contraseña) VALUES ('" +
                nombreUsu + "', '" + tipoCuenta + "', '" + avatar + "', '" + contraseña + "')");
    }

    private void insertarEstadistica(SQLiteDatabase db, int porcentaje, int tabla, String multiplicacionFallida, String fecha, int avatarJugado, int idUsuario) {
        db.execSQL("INSERT INTO ESTADISTICAS (porcentaje, tabla, tablas_fallidas, fecha, avatarJugado, id_usuario) VALUES (" +
                porcentaje + ", " + tabla + ", '" + multiplicacionFallida + "', '" + fecha + "', " + avatarJugado + ", " + idUsuario + ")");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USUARIO");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ESTADISTICAS");
        onCreate(sqLiteDatabase);
    }
}
