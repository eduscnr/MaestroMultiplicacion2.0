package com.example.maestromultiplicacion20.ui.estadisticas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.modelo.Estadisticas;

import java.util.List;

public class AdaptadorTablas extends ArrayAdapter<Estadisticas> {
    List<Estadisticas> estadisticas;
    String [] nose;
    Context mContext;
    public AdaptadorTablas(@NonNull Context context, int resource, List<Estadisticas> estadisticas) {
        super(context, resource, estadisticas);
        mContext = context;
        this.estadisticas = estadisticas;
        System.out.println(estadisticas);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return crearFilaPersonalizada(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
        return crearFilaPersonalizada(position, convertView, parent);
    }
    private View crearFilaPersonalizada(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View viewFecha = inflater.inflate(R.layout.seleccion_tabla, parent, false);
        TextView textFecha = viewFecha.findViewById(R.id.seleccionTabla);
        textFecha.setText("Tabla: " + estadisticas.get(position).getTablaSeleccionada());
        return viewFecha;
    }

    public void setEstadisticas(List<Estadisticas> estadisticas) {
        this.estadisticas = estadisticas;
    }
}
