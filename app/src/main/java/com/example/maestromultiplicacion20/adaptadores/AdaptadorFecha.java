package com.example.maestromultiplicacion20.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.maestromultiplicacion20.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase adptados para las fechas
 */
public class AdaptadorFecha extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> fechas;
    public AdaptadorFecha(Context context, int resource, List<String> object) {
        super(context, resource, object);
        mContext = context;
        fechas = new ArrayList<>(object);
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
        View viewFecha = inflater.inflate(R.layout.seleccion_fecha, parent, false);
        TextView textFecha = viewFecha.findViewById(R.id.seleccionFecha);
        textFecha.setText(fechas.get(position));
        return viewFecha;
    }
}
