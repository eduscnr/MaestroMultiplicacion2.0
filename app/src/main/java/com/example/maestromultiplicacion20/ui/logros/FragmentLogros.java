package com.example.maestromultiplicacion20.ui.logros;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.maestromultiplicacion20.databinding.FragmentLogrosBinding;
import com.example.maestromultiplicacion20.inicio.ActividadNavegationDrawer;
import com.example.maestromultiplicacion20.R;

/**
 * Fragmento para mostrar los avateres conseguido o logros
 */
public class FragmentLogros extends Fragment {
    private FragmentLogrosBinding binding;
    private GridLayout gridLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogrosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        gridLayout = root.findViewById(R.id.gridImageView);
        mostrarImagenesGrid();
        return root;
    }
    private void mostrarImagenesGrid(){
        gridLayout = binding.getRoot().findViewById(R.id.gridImageView);
        ImageView imgView;
        for(int i = 0; i< ActividadNavegationDrawer.getAvataresFinales().size(); i++){
            imgView = new ImageView(requireContext());
            if(ActividadNavegationDrawer.getAvataresColeccionables().contains(ActividadNavegationDrawer.getAvataresFinales().get(i))){
                imgView.setImageResource(ActividadNavegationDrawer.getAvataresFinales().get(i));
            }else{
                imgView.setImageResource(ActividadNavegationDrawer.getAvataresFinales().get(i));
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0F);
                ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
                imgView.setColorFilter(colorFilter);
            }
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(340, 340);
            imgView.setLayoutParams(layoutParams);
            gridLayout.addView(imgView, i);
        }
    }
}