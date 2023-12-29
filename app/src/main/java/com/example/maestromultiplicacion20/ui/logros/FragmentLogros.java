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

import com.example.maestromultiplicacion20.MainActivity;
import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.databinding.FragmentLogrosBinding;

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
        for(int i = 0; i< MainActivity.getAvataresFinales().size(); i++){
            imgView = new ImageView(requireContext());
            if(MainActivity.getAvataresColeccionables().contains(MainActivity.getAvataresFinales().get(i))){
                imgView.setImageResource(MainActivity.getAvataresFinales().get(i));
            }else{
                imgView.setImageResource(MainActivity.getAvataresFinales().get(i));
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0F);
                ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
                imgView.setColorFilter(colorFilter);
            }
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(380, 380);
            imgView.setLayoutParams(layoutParams);
            gridLayout.addView(imgView, i);
        }
    }
}