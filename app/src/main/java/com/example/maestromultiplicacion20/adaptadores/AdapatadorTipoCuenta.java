package com.example.maestromultiplicacion20.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.maestromultiplicacion20.R;

public class AdapatadorTipoCuenta extends ArrayAdapter<String> {
    private Context context;
    private String[] tipoCuentas= {"administrador", "usuario"};
    public AdapatadorTipoCuenta(Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        this.context = context;
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View viewCuenta = inflater.inflate(R.layout.seleccion_cuenta, parent, false);
        TextView textCuenta = viewCuenta.findViewById(R.id.seleccionCuenta);
        textCuenta.setText(tipoCuentas[position]);
        return viewCuenta;
    }
}
