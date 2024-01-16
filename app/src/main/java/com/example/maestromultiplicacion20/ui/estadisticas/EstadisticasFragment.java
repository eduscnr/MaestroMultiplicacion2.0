package com.example.maestromultiplicacion20.ui.estadisticas;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.adaptadores.AdaptadorFecha;
import com.example.maestromultiplicacion20.adaptadores.AdaptadorTablas;
import com.example.maestromultiplicacion20.adaptadores.AdaptadorUsuarios;
import com.example.maestromultiplicacion20.databinding.FragmentEstadisticasBinding;
import com.example.maestromultiplicacion20.modelo.Estadisticas;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.example.maestromultiplicacion20.database.EstadisticasDAOImpl;
import com.example.maestromultiplicacion20.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class EstadisticasFragment extends Fragment implements Spinner.OnItemSelectedListener{

    private FragmentEstadisticasBinding binding;
    private List<String> fechas;
    private List<String> usuarios;
    private List<Estadisticas> estadisticas;
    private AdaptadorTablas aTablas;
    private AdaptadorFecha aFecha;
    private Spinner spinnerTabla;
    private Spinner spinnerUsuario;
    private Spinner spinnerFecha;
    private EstadisticasDAO estadisticasDAO;
    private String usuarioSeleccionado;
    private GridLayout gridLayoutMulFallida;
    private ProgressBar progressBar;
    private TextView procentajeTv;
    private ImageView avatarJugado;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEstadisticasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button btnEliminarEsta = root.findViewById(R.id.btnEliminar);
        gridLayoutMulFallida = root.findViewById(R.id.gridMultiplicaciones);
        procentajeTv = root.findViewById(R.id.tvPorcentajeExito);
        progressBar = root.findViewById(R.id.pbExito);
        avatarJugado = root.findViewById(R.id.mostarAvatarSeleccionado);
        usuarios = new ArrayList<>();
        //Motrar funcionalidad de los spinner y las estadisticas.
        estadisticasDAO = new EstadisticasDAOImpl(requireContext());
        spinnerFecha = root.findViewById(R.id.spinnerFecha);
        spinnerTabla = root.findViewById(R.id.spinnerTablas);
        spinnerUsuario = root.findViewById(R.id.spinnerUsuario);
        for (Usuario u: estadisticasDAO.obtenerUsuarios()){
            if(u.getTipoCuenta().equalsIgnoreCase("usuario")){
                usuarios.add(u.getNombreUsuario());
            }
        }
        AdaptadorUsuarios aUsuario = new AdaptadorUsuarios(requireContext(), R.layout.seleccion_usuario, usuarios);
        spinnerUsuario.setAdapter(aUsuario);
        spinnerTabla.setOnItemSelectedListener(this);
        spinnerFecha.setOnItemSelectedListener(this);
        spinnerUsuario.setOnItemSelectedListener(this);
        btnEliminarEsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estadisticasDAO.eliminarEstadisticas();
                aTablas.clear();
                aFecha.clear();
                gridLayoutMulFallida.removeAllViews();
                progressBar.setProgress(0);
                procentajeTv.setText("0%");
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spinnerUsuario){
            usuarioSeleccionado = usuarios.get(i);
            fechas = estadisticasDAO.obtenerFechas(usuarioSeleccionado);
            aFecha = new AdaptadorFecha(requireContext(), R.layout.seleccion_fecha, fechas);
            spinnerFecha.setAdapter(aFecha);
        } else if(adapterView.getId() == R.id.spinnerFecha){
            String fecha = fechas.get(i);
            estadisticas = estadisticasDAO.obtenerEstadisticas(fecha, usuarioSeleccionado);
            System.out.println("Estadisticas: " +estadisticas);
            aTablas = new AdaptadorTablas(requireContext(), R.layout.seleccion_tabla, estadisticas);
            spinnerTabla.setAdapter(aTablas);
        }else if(adapterView.getId() == R.id.spinnerTablas){
            gridLayoutMulFallida.removeAllViews();
            mostrarMultiplicacionesFallidas(estadisticas.get(i).getMultiplicacionesFallidas());
            progressBar.setProgress(Integer.parseInt(estadisticas.get(i).getPorcetajeDeExito()));
            procentajeTv.setText(estadisticas.get(i).getPorcetajeDeExito()+"%");
            avatarJugado.setImageResource(estadisticas.get(i).getAvatarSeleccionado());
        }
    }

    private void mostrarMultiplicacionesFallidas(List<String> multiplicacionesFallidas) {
        if(!multiplicacionesFallidas.isEmpty()){
            for (int i = 0;i<multiplicacionesFallidas.size();i++){
                TextView tv = new TextView(requireContext());
                tv.setTextSize(20);
                tv.setTextColor(Color.BLACK);
                tv.setText(multiplicacionesFallidas.get(i));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.setMargins(0, 0, 8, 0);
                tv.setLayoutParams(params);
                gridLayoutMulFallida.addView(tv);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}