package com.example.maestromultiplicacion20.ui.configuracion;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.maestromultiplicacion20.inicio.ActividadNavegationDrawer;
import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.adaptadores.AdaptadorAvatares;
import com.example.maestromultiplicacion20.adaptadores.AdaptadorDificultad;
import com.example.maestromultiplicacion20.databinding.FragmentConfiguracionBinding;

import java.util.Random;

public class ConfiguracionFragment extends Fragment implements Spinner.OnItemSelectedListener,View.OnClickListener{

    private FragmentConfiguracionBinding binding;
    private String[] nombreHeroes = { "Superman", "Batman", "Ironman", "Spiderman", "Thor" };
    private String [] dificultades = {"Fácil", "Media", "Dificil"};
    private int tablaMultiplicar = 2;
    private int colorAplicacion;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentConfiguracionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Spinner pruebaSpinner = root.findViewById(R.id.spinner);
        Spinner dificultadSpinner = root.findViewById(R.id.spinnerDificultad);
        AdaptadorAvatares adapter = new AdaptadorAvatares(getContext(), R.layout.seleccion_heroes, nombreHeroes);
        AdaptadorDificultad spinnerDificultad = new AdaptadorDificultad(getContext(), R.layout.seleccion_dificultad, dificultades);
        pruebaSpinner.setAdapter(adapter);
        dificultadSpinner.setAdapter(spinnerDificultad);
        pruebaSpinner.setOnItemSelectedListener(this);
        dificultadSpinner.setOnItemSelectedListener(this);
        aniadirHijos(12);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int posicion, long l) {
        if (adapterView.getId() == R.id.spinner) {
            ActividadNavegationDrawer.setAvatar(posicion);
            //Dependiendo de la posición que haya seleccionado cambia de un color u otro a través del avatar
            switch (posicion) {
                case 0:
                    colorAplicacion = ContextCompat.getColor(requireContext(), R.color.colorSuperman);
                    actualizarColores(colorAplicacion);
                    recorrer();
                    break;
                case 1:
                    colorAplicacion = ContextCompat.getColor(requireContext(), R.color.colorBatman);
                    actualizarColores(colorAplicacion);
                    recorrer();
                    break;
                case 2:
                    colorAplicacion = ContextCompat.getColor(requireContext(), R.color.colorIronman);
                    actualizarColores(colorAplicacion);
                    recorrer();
                    break;
                case 3:
                    colorAplicacion = ContextCompat.getColor(requireContext(), R.color.colorSpiderman);
                    actualizarColores(colorAplicacion);
                    recorrer();
                    break;
                case 4:
                    colorAplicacion = ContextCompat.getColor(requireContext(), R.color.colorThor);
                    actualizarColores(colorAplicacion);
                    recorrer();
                    break;
            }
        } else if (adapterView.getId() == R.id.spinnerDificultad) {
            String dificultadSeleccionada = dificultades[posicion];
            ActividadNavegationDrawer.setDificultad(dificultadSeleccionada);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void recorrer(){

        GridLayout gridLayout = binding.getRoot().findViewById(R.id.gridBotonera);
        Button b;
        for(int i =0;i< gridLayout.getChildCount();i++){
            View v;
            v=gridLayout.getChildAt(i);
            if(v.getClass().getSimpleName().equals("Button")){
                b = (Button) v;
                b.setBackgroundColor(colorAplicacion);
            }
        }

    }
    public void aniadirHijos(int i){
        GridLayout g = (GridLayout) binding.getRoot().findViewById(R.id.gridBotonera);
        Button b;
        for(int j=0;j<i;j++) {
            b = new Button(this.getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(8,8,8,8);
            b.setLayoutParams(params);
            if(j == i-1){
                b.setText("?");
            }else{
                b.setText(String.valueOf(j));
            }
            b.setTextColor(Color.WHITE);
            b.setId(j);
            b.setOnClickListener(this);
            g.addView(b,j);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getClass().getSimpleName().equals("Button")){
            Button b = (Button) view;
            /*Uso este método aquí también para quitar la selección de un boton para que vuelva
            al color de la aplicación*/
            recorrer();
            //Añado un color al boton que se a seleccionado.
            b.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBtnSeleccionado));
            if(b.getText().toString().equals("?")){
                tablaMultiplicar = new Random().nextInt(10);
                ActividadNavegationDrawer.setTablaMultiplicar(tablaMultiplicar);
            }else{
                tablaMultiplicar = Integer.parseInt(b.getText().toString());
                ActividadNavegationDrawer.setTablaMultiplicar(tablaMultiplicar);
            }
            ActividadNavegationDrawer.setTablaTemporalSeleccionada(-1);
        }
    }
    //Método que cambia de color la barra de estados (con Window) y el ActionBar
    private void actualizarColores(int color) {
        Window window = getActivity().getWindow();
        window.setStatusBarColor(color);
        //Compruebo si la actividad hereda de AppCompatActivity si es así cambio de color la barra de acción
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        }
        ActividadNavegationDrawer.setColorAplicacion(color);
    }
}