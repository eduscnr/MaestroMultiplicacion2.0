package com.example.maestromultiplicacion20.inicio;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.database.EstadisticasDAOImpl;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.example.maestromultiplicacion20.modelo.Usuario;

public class ActividadCrearCuentas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_crear_cuentas);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        EditText edUsuario = findViewById(R.id.edtNombreUsuario);
        EditText contrasenia = findViewById(R.id.edtContrasenia);
        Button ingresar = findViewById(R.id.btnIngresar);
        TextView textInformacion = findViewById(R.id.tvInformacion);
        textInformacion.setVisibility(View.GONE);
        EstadisticasDAO estadisticasDAO = new EstadisticasDAOImpl(this);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario = estadisticasDAO.obtenerUsuario(edUsuario.getText().toString());
                if(usuario == null){
                    textInformacion.setVisibility(View.VISIBLE);
                    textInformacion.setText("El usuario no existe");
                }
                else if(usuario.getContrasenia().equalsIgnoreCase(contrasenia.getText().toString())){
                    setResult(RESULT_OK);
                    finish();
                }else{
                    textInformacion.setVisibility(View.VISIBLE);
                    textInformacion.setText("Contraseña incorrecta");
                }
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setResult(RESULT_CANCELED);
                finish();
            }
        };
    }
}