package com.example.maestromultiplicacion20.ui.configuracion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.maestromultiplicacion20.R;


public class AdaptadorDificultad extends ArrayAdapter<String> {
    String [] dificultades = {"Fácil", "Media", "Dificil"};
    Context mContext;
    public AdaptadorDificultad(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        mContext = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return crearFilaPersonalizada(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
        return crearFilaPersonalizada(position, convertView, parent);
    }
    public View crearFilaPersonalizada(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dificultadView = inflater.inflate(R.layout.seleccion_dificultad, parent, false);
        TextView dificultadTxv = dificultadView.findViewById(R.id.dificultad);
        dificultadTxv.setText(dificultades[position]);
        return dificultadView;
    }
}
