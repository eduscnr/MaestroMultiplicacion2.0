package com.example.maestromultiplicacion20.ui.contactos;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.adaptadores.AdaptadorContactos;
import com.example.maestromultiplicacion20.databinding.FragmentContactosBinding;
import com.example.maestromultiplicacion20.interfaces.ContactosOnClick;
import com.example.maestromultiplicacion20.modelo.Contacto;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento para visualizar los contactos a traves de un Adaptador
 */
public class FragmentContactos extends Fragment implements ContactosOnClick {
    private FragmentContactosBinding binding;
    private RecyclerView recyclerView;
    //private EditText editText;
    private ActivityResultLauncher<String> requestReadContactsPermissionLauncher;
    private ActivityResultLauncher<String> requestWriteContactsPermissionLauncher;
    private AdaptadorContactos adaptadorContactos;
    private List<Contacto> contactos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContactosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.recyclerViewContactos);
        // Recoger el resultado del permiso y si lo acepta solicito el permiso de escritura
        requestReadContactsPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isReadContactsGranted -> {
                    if (isReadContactsGranted) {
                        solicitarPermisoEscritura();
                    } else {
                        System.out.println("Permiso lectura denegado, no se puede mostrar contenido de la aplicación");
                    }
                });
        //Recoger el resultado del permiso de escritua y si lo acepta muestra el contenido en el RecyclerView
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
     * Método para obtener los contacto de la agenda del movil a traves de una URI y de un Contents Providers
     * @return devuelve una lista de contactos
     */
    private List<Contacto> obtenerContactos(String nombreBusqueda) {
        List<Contacto> devolver = new ArrayList<>();
        String[] columnas = new String[]{
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.STARRED
        };
        String seleccion = ContactsContract.Data.DISPLAY_NAME + " LIKE ?";
        String[] argumentosSeleccion = new String[]{"%" + nombreBusqueda + "%"};

        //Uso un cursor para recorrer la URI, lo ordeno de forma descender los contactos favoritos y los que no sea favoritos
        //lo ordeno de forma ascendente
        Cursor cursor = requireContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                columnas,
                seleccion,
                argumentosSeleccion,
                ContactsContract.CommonDataKinds.Phone.STARRED + " DESC, " +
                        ContactsContract.Data.DISPLAY_NAME + " ASC"
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String nombre = cursor.getString(0);
                String numero = cursor.getString(1);
                int contactId = cursor.getInt(2);
                boolean favorito = cursor.getInt(3) == 1 ? true : false;
                devolver.add(new Contacto(nombre, numero, contactId, favorito));
            }
            cursor.close();
        }
        return devolver;
    }

    /**
     * Método para mostar los contactos en el RecyclerView con su CardView y el Adaptador
     */
    private void mostrarContactos() {
        contactos = obtenerContactos("");
        adaptadorContactos = new AdaptadorContactos(contactos, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adaptadorContactos);
        binding.editTextContacto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                contactos = obtenerContactos(binding.editTextContacto.getText().toString());
                adaptadorContactos = new AdaptadorContactos(contactos, null);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                recyclerView.setAdapter(adaptadorContactos);
                return true;
            }
        });
    }

    /**
     * Método para solicitar el permiso de lectura de los contactos
     */
    private void solictarPermisoLectura() {
        //Si ha sido aceptado el permiso de lectura, solicitamos el de escritura y si ya estan aceptados tanto el de lectura
        //como el de escritura, mostrar el contenido
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
     * Método para solicitar el permiso de escritura de los contactos
     */
    private void solicitarPermisoEscritura() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestWriteContactsPermissionLauncher.launch(Manifest.permission.WRITE_CONTACTS);
        }
    }
    @Override
    public void onClickContactos(int posicion) {
        Contacto c = contactos.get(posicion);
        agregarFavorito(c.getId());
    }

    /**
     * Método para guarda a favoritos los contactos
     * @param id id del contactos de la URI
     */
    private void agregarFavorito(int id) {
        ContentValues cv =new ContentValues();
        cv.put(ContactsContract.Contacts.STARRED, 1);
        requireContext().getContentResolver().update(
                ContactsContract.Contacts.CONTENT_URI,
                cv,
                ContactsContract.Contacts._ID + " = ?",
                new String[]{String.valueOf(id)});
        Toast.makeText(requireContext(), "Contacto agregado como favorito", Toast.LENGTH_SHORT).show();
    }
}