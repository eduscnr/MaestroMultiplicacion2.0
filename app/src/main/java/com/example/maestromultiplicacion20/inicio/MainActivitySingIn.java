package com.example.maestromultiplicacion20.inicio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.maestromultiplicacion20.MainActivity;
import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.example.maestromultiplicacion20.database.EstadisticasDAOImpl;
import com.example.maestromultiplicacion20.modelo.Usuario;

public class MainActivitySingIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sing_in);
        EditText edUsuario = findViewById(R.id.edtNombreUsuario);
        EditText contrasenia = findViewById(R.id.edtContrasenia);
        Button ingresar = findViewById(R.id.btnIngresar);
        edUsuario.setFocusable(false);
        edUsuario.setText(MainActivityPrincipal.getUsuarioLogeado().getNombreUsuario());
        EstadisticasDAO estadisticasDAO = new EstadisticasDAOImpl(this);
        Usuario usuario = estadisticasDAO.obtenerUsuario(edUsuario.getText().toString());
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usuario.getContrasenia().equalsIgnoreCase(contrasenia.getText().toString())){
                    Intent i = new Intent(MainActivitySingIn.this, MainActivity.class);
                    MainActivitySingIn.this.startActivity(i);
                }
            }
        });
    }
}