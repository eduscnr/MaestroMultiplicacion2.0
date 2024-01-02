package com.example.maestromultiplicacion20.ui.contactosFavoritos;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.adaptadores.AdaptadorContactos;
import com.example.maestromultiplicacion20.databinding.FragmentContactosBinding;
import com.example.maestromultiplicacion20.databinding.FragmentContactosFavBinding;
import com.example.maestromultiplicacion20.interfaces.ContactosOnClick;
import com.example.maestromultiplicacion20.modelo.Contacto;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento para mostrar los contactos favoritos
 */
public class FragmentContactosFav extends Fragment implements ContactosOnClick {
    private FragmentContactosFavBinding binding;
    private RecyclerView recyclerView;
    private AdaptadorContactos adaptadorContactos;
    private List<Contacto> contactos;
    private ActivityResultLauncher<String> requestReadContactsPermissionLauncher;
    private ActivityResultLauncher<String> requestWriteContactsPermissionLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContactosFavBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.recyclerViewConFav);
        requestReadContactsPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isReadContactsGranted -> {
                    if (isReadContactsGranted) {
                        solicitarPermisoEscritura();
                    } else {
                        System.out.println("Permiso lectura denegado, no se puede mostrar contenido de la aplicación");
                    }
                });
        requestWriteContactsPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isWriteContactsGranted -> {
                    if (isWriteContactsGranted) {
                        mostrarContactos();
                    } else {
                        System.out.println("Permiso de escritura denegado, no se puede mostrar el contenido :(");
                    }
                });
        solictarPermisoLectura();
        return root;
    }

    /**
     * Método para solicitar el permiso de lectura de los contactos
     */
    private void solictarPermisoLectura() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestReadContactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
        }else if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CONTACTS)
                == PackageManager.PERMISSION_GRANTED){
            mostrarContactos();
        }
    }

    /**
     * Método para solicitar el permiso de escritura
     */
    private void solicitarPermisoEscritura() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestWriteContactsPermissionLauncher.launch(Manifest.permission.WRITE_CONTACTS);
        }
    }

    /**
     * Método para mostrar los contactos en el RecyclerView
     */
    private void mostrarContactos() {
        contactos = obtenerContactosFavoritos();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setHasFixedSize(true);
        adaptadorContactos = new AdaptadorContactos(contactos, this);
        recyclerView.setAdapter(adaptadorContactos);
    }

    /**
     * Método para obtener los contactos favoritos
     * @return devuelve una lista de contactos
     */
    private List<Contacto> obtenerContactosFavoritos() {
        List<Contacto> devolver = new ArrayList<>();
        String[] columnas = new String[]{
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        };

        String seleccion = ContactsContract.CommonDataKinds.Phone.STARRED + "=?";
        String[] seleccionArgs = new String[]{"1"}; // "1" indica que el contacto está marcado como favorito

        Cursor cursor = requireContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                columnas,
                seleccion,
                seleccionArgs,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String nombre = cursor.getString(0);
                String numero = cursor.getString(1);
                int contactId = cursor.getInt(2);
                devolver.add(new Contacto(nombre, numero, contactId));
            }
            cursor.close();
        }
        for (Contacto c: devolver){
            c.setFavorito(true);
        }
        return devolver;
    }

    /**
     * Método para quitar de favoritos un contactos
     * @param id id del contacto
     */
    private void quitarFavorito(int id) {
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Contacts.STARRED, 0); // 1 para favorito, 0 para no favorito

        requireContext().getContentResolver().update(
                ContactsContract.Contacts.CONTENT_URI,
                values,
                ContactsContract.Contacts._ID + " = ?",
                new String[]{String.valueOf(id)});
    }
    @Override
    public void onClickContactos(int posicion) {
        Contacto contacto = contactos.get(posicion);
        contacto.setFavorito(false);
        quitarFavorito(contacto.getId());

        // Guardo el elemento eliminado para realizar un delayed
        Contacto elementoEliminado = contactos.remove(posicion);
        // notifico al adaptador para que actualize sus elementos
        adaptadorContactos.notifyItemRemoved(posicion);
        // Aplico la animación del elemento elimanado
        recyclerView.postDelayed(() -> {
            int posicionFinal = contactos.indexOf(elementoEliminado);
            if (posicionFinal != -1) {
                adaptadorContactos.notifyDataSetChanged();
            }
        }, 500);
    }

}