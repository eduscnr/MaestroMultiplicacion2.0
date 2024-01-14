package com.example.maestromultiplicacion20.ui.eliminarUsuarios;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.adaptadores.AdaptadorEliminarUsuarios;
import com.example.maestromultiplicacion20.adaptadores.AdaptadorUsuarios;
import com.example.maestromultiplicacion20.adaptadores.UsuarioPersonalizadoAdapter;
import com.example.maestromultiplicacion20.database.EstadisticasDAOImpl;
import com.example.maestromultiplicacion20.databinding.FragmentEliminarUsuarioBinding;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.example.maestromultiplicacion20.modelo.Usuario;
import com.example.maestromultiplicacion20.modelo.UsuarioPersonalizado;

import java.util.ArrayList;
import java.util.List;

public class FragmentEliminarUsuario extends Fragment {
    private FragmentEliminarUsuarioBinding binding;
    private RecyclerView recyclerView;
    private List<Usuario> usuarios;
    private List<UsuarioPersonalizado> itemList;
    private AdaptadorEliminarUsuarios adaptadorUsuarios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEliminarUsuarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        EstadisticasDAO estadisticasDAO = new EstadisticasDAOImpl(requireContext());
        usuarios = estadisticasDAO.obtenerUsuarios();
        itemList = apariencia();
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        adaptadorUsuarios = new AdaptadorEliminarUsuarios(itemList, requireContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adaptadorUsuarios);
        return root;
    }
    public List<UsuarioPersonalizado> apariencia(){
        List<UsuarioPersonalizado> itemList = new ArrayList<>();
        if(!usuarios.isEmpty()) {
            for (Usuario u : usuarios) {
                itemList.add(new UsuarioPersonalizado(u.getAvatarImg(), u.getNombreUsuario()));
            }
        }
        return itemList;
    }
}