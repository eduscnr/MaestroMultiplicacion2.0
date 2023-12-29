package com.example.maestromultiplicacion20.ui.enviarEstadisticas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.example.maestromultiplicacion20.database.EstadisticasDAOImpl;
import com.example.maestromultiplicacion20.databinding.FragmentEnviarBinding;
import com.example.maestromultiplicacion20.modelo.Estadisticas;
import com.example.maestromultiplicacion20.modelo.Usuario;
import com.example.maestromultiplicacion20.ui.estadisticas.AdaptadorUsuarios;

import java.util.ArrayList;
import java.util.List;

public class FragmentEnviar extends Fragment implements Spinner.OnItemSelectedListener{
    private Button btnEstadisticas;
    private EditText edEmail;
    private FragmentEnviarBinding binding;
    private List<Estadisticas> estadisticas;
    private EstadisticasDAO estadisticasDAO;
    private List<String> usuarios;
    public FragmentEnviar() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEnviarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        estadisticas = new ArrayList<>();
        edEmail = root.findViewById(R.id.etEmail);
        btnEstadisticas = root.findViewById(R.id.btnEnviar);
        Spinner spinnerUsuario = root.findViewById(R.id.spinnerUsuario);
        estadisticasDAO = new EstadisticasDAOImpl(requireContext());
        usuarios = new ArrayList<>();
        for (Usuario u: estadisticasDAO.obtenerUsuarios()){
            if(u.getTipoCuenta().equalsIgnoreCase("usuario")){
                usuarios.add(u.getNombreUsuario());
            }
        }
        AdaptadorUsuarios aUsuario = new AdaptadorUsuarios(requireContext(), R.layout.seleccion_usuario,usuarios);
        spinnerUsuario.setAdapter(aUsuario);
        spinnerUsuario.setOnItemSelectedListener(this);
        btnEstadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent();
                Intent chooser=null;
                if(!edEmail.getText().toString().equalsIgnoreCase("")){
                    i.setAction(Intent.ACTION_SEND);
                    i.setData(Uri.parse("mailto:"));
                    i.putExtra(Intent.EXTRA_SUBJECT, "Estadísticas de Multiplicación");
                    String para[]={edEmail.getText().toString()};
                    i.putExtra(Intent.EXTRA_EMAIL, para);
                    StringBuilder mensaje = new StringBuilder("Estadísticas de Multiplicación:\n\n");
                    for (Estadisticas es : estadisticas) {
                        mensaje.append("Tabla: ").append(es.getTablaSeleccionada()).append("\n");
                        mensaje.append("Porcentaje de Éxito: ").append(es.getPorcetajeDeExito()).append("\n");
                        mensaje.append("Fecha: ").append(es.convertirFecha()).append("\n");
                        mensaje.append("Multiplicaciones Fallidas: ").append(es.getMultiplicacionesFallidas()).append("\n\n");
                    }
                    i.putExtra(Intent.EXTRA_TEXT, mensaje.toString());
                    i.setType("message/rfc822");
                    chooser=i.createChooser(i,"Enviar Email");
                    if (chooser.resolveActivity(requireContext().getPackageManager()) != null) {
                        startActivity(chooser);
                        Toast.makeText(getContext(), "Enviando estadísticas por correo electrónico", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "No hay ninguna aplicación de correo electrónico instalada", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return root;
    }
    @Override
    public void onStop() {
        super.onStop();
        edEmail.setText("");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spinnerUsuario){
            estadisticas = estadisticasDAO.obtenerEstadisticasUsuario(usuarios.get(i));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}