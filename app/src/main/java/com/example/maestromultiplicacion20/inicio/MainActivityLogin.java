package com.example.maestromultiplicacion20.inicio;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.adaptadores.AdapatadorTipoCuenta;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.example.maestromultiplicacion20.database.EstadisticasDAOImpl;

import kotlin.InitializedLazyImpl;

/**
 * Clase para crear nuevos usuarios
 */
public class MainActivityLogin extends AppCompatActivity implements Spinner.OnItemSelectedListener{
    private String[] tipoCuentas= {"administrador", "usuario"};
    private Spinner spinnerCuenta;
    private AdapatadorTipoCuenta aTipoCuenta;
    private EditText contrasenia;
    private String cuenta;
    private EstadisticasDAO estadisticasDAO;
    private TextView textViewInformacion;
    private ActivityResultLauncher<Intent> actividadResultUsuario;
    private ActivityResultLauncher<Intent> actividadResultadoAdministrador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        estadisticasDAO = new EstadisticasDAOImpl(this);
        EditText usuario = findViewById(R.id.edtNombreUsuario);
        Button enviar = findViewById(R.id.btnRegistrar);
        textViewInformacion = findViewById(R.id.tvInformacion);
        textViewInformacion.setVisibility(View.GONE);
        contrasenia = findViewById(R.id.edtContrasenia);
        spinnerCuenta = findViewById(R.id.spinnerTipCuenta);
        aTipoCuenta = new AdapatadorTipoCuenta(this, R.layout.seleccion_cuenta, tipoCuentas);
        spinnerCuenta.setAdapter(aTipoCuenta);
        spinnerCuenta.setOnItemSelectedListener(this);
        //onClick en el boton para realizar la creacion de un nuevo usuario y enviarselo a la actividad MainPrincipal
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contrasenia.getVisibility() == View.VISIBLE && !contrasenia.getText().toString().equalsIgnoreCase("")){
                    if(!usuario.getText().toString().equalsIgnoreCase("")){
                        textViewInformacion.setVisibility(View.GONE);
                        Intent intent = new Intent(MainActivityLogin.this, ActividadCrearCuentas.class);
                        actividadResultadoAdministrador.launch(intent);
                    }else{
                        textViewInformacion.setVisibility(View.VISIBLE);
                        textViewInformacion.setText("El campo contraseña y nombre no puede estar vacios");
                    }
                }else{
                    if(!usuario.getText().toString().equalsIgnoreCase("") && contrasenia.getVisibility() == View.GONE){
                            textViewInformacion.setVisibility(View.GONE);
                            Intent intent = new Intent(MainActivityLogin.this, ActividadCrearCuentas.class);
                            actividadResultUsuario.launch(intent);
                    }else{
                        textViewInformacion.setVisibility(View.VISIBLE);
                        textViewInformacion.setText("El campo nombre no puede estar vacios");
                    }
                }
            }
        });
        actividadResultUsuario = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK){
                        String registrado = estadisticasDAO.registrarUsuario(usuario.getText().toString(), null, cuenta, R.drawable.icons8_usuario_48__1_);
                        if(!registrado.equalsIgnoreCase("Usuario ya registrado")){
                            textViewInformacion.setVisibility(View.GONE);
                            System.out.println("he pasado por aqui y no se porque?");
                            MainActivityPrincipal.getUsuarios().clear();
                            MainActivityPrincipal.setUsuarios(estadisticasDAO.obtenerUsuarios());
                            Intent i = new Intent();
                            i.putExtra("Usuario", usuario.getText().toString());
                            setResult(RESULT_OK, i);
                            finish();
                        }else{
                            textViewInformacion.setVisibility(View.VISIBLE);
                            textViewInformacion.setText(registrado);
                        }
                    }
                }
        );
        actividadResultadoAdministrador = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK){
                        String registrado = estadisticasDAO.registrarUsuario(usuario.getText().toString(), contrasenia.getText().toString(), cuenta, R.drawable.icons8_usuario_48__1_);
                        if(!registrado.equalsIgnoreCase("Usuario ya registrado")){
                            textViewInformacion.setVisibility(View.GONE);
                            MainActivityPrincipal.getUsuarios().clear();
                            MainActivityPrincipal.setUsuarios(estadisticasDAO.obtenerUsuarios());
                            Intent i = new Intent();
                            i.putExtra("Usuario", usuario.getText().toString());
                            setResult(RESULT_OK, i);
                            finish();
                        }else{
                            textViewInformacion.setVisibility(View.VISIBLE);
                            textViewInformacion.setText(registrado);
                        }
                    }
                }
        );

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setResult(RESULT_CANCELED);
                finish();
            }
        };
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spinnerTipCuenta){
            if(tipoCuentas[i].equalsIgnoreCase("administrador")){
                cuenta = tipoCuentas[i];
                contrasenia.setVisibility(View.VISIBLE);
            }else{
                cuenta = tipoCuentas[i];
                contrasenia.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}