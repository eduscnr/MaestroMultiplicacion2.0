package com.example.maestromultiplicacion20.inicio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.example.maestromultiplicacion20.database.EstadisticasDAOImpl;
import com.example.maestromultiplicacion20.modelo.Usuario;

/**
 * Clase para iniciar sesio las cuentas admin
 */

public class ActividadSingUp extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sing_up);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        EditText edUsuario = findViewById(R.id.edtNombreUsuario);
        EditText contrasenia = findViewById(R.id.edtContrasenia);
        Button ingresar = findViewById(R.id.btnIngresar);
        TextView textInformacion = findViewById(R.id.tvInformacion);
        textInformacion.setVisibility(View.GONE);
        edUsuario.setFocusable(false);
        edUsuario.setText(ActividadPrincipal.getUsuarioLogeado().getNombreUsuario());
        EstadisticasDAO estadisticasDAO = new EstadisticasDAOImpl(this);
        //onClick para iniciar sesion en esa cuenta
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario = estadisticasDAO.obtenerUsuario(edUsuario.getText().toString());
                if(usuario.getContrasenia().equalsIgnoreCase(contrasenia.getText().toString())){
                    Intent i = new Intent(ActividadSingUp.this, ActividadNavegationDrawer.class);
                    ActividadSingUp.this.startActivity(i);
                    finish();
                }else{
                    textInformacion.setVisibility(View.VISIBLE);
                    textInformacion.setText("Contraseña incorrecta");
                }
            }
        });
    }
}