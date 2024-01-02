package com.example.maestromultiplicacion20.ui.estadisticas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.maestromultiplicacion20.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase Adaptador para los usuarios
 */
public class AdaptadorUsuarios extends ArrayAdapter<String> {
    private Context context;
    private List<String> usuarios;
    public AdaptadorUsuarios(@NonNull Context context, int resource,List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        usuarios = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return crearFilaPersonalizada(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return crearFilaPersonalizada(position, convertView, parent);
    }

    private View crearFilaPersonalizada(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View usuarioView = inflater.inflate(R.layout.seleccion_usuario, parent,false);
        TextView textViewUsu = usuarioView.findViewById(R.id.seleccionUsuario);
        textViewUsu.setText(usuarios.get(position));
        return usuarioView;
    }
}
