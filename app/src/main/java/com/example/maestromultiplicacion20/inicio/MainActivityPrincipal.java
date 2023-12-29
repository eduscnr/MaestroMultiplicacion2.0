package com.example.maestromultiplicacion20.inicio;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.example.maestromultiplicacion20.database.EstadisticasDAOImpl;
import com.example.maestromultiplicacion20.database.Sqlite;
import com.example.maestromultiplicacion20.modelo.Estadisticas;
import com.example.maestromultiplicacion20.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MainActivityPrincipal extends AppCompatActivity {
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private List<Item> itemList;
    private ItemAdapter adapter;
    private EstadisticasDAO estadisticasDAO;
    private RecyclerView recyclerView;
    private static List<Usuario> usuarios;
    private static Usuario usuarioLogeado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_principal);
        Sqlite sqlite = new Sqlite(this);
        estadisticasDAO = new EstadisticasDAOImpl(this);
        sqlite.getWritableDatabase();
        usuarios = estadisticasDAO.obtenerUsuarios();
        List<Estadisticas> estadisticas = estadisticasDAO.obtenerEstadisticas("16/12/2023", "Usuario1");
        System.out.println("Prueba: "+estadisticas);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        itemList = obtenerDatos(); // Método ficticio para obtener datos
        adapter = new ItemAdapter(itemList, this);
        recyclerView.setHasFixedSize(true);
        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemButtonClick() {
                Intent intent = new Intent(MainActivityPrincipal.this, MainActivityLogin.class);
                activityResultLauncher.launch(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String usuario = data.getStringExtra("Usuario");
                            agregarNuevoElemento(usuario);
                        }
                    }
                }
        );
    }
    private List<Item> obtenerDatos() {
        List<Item> itemList = new ArrayList<>();
        //Uso esto porque juego con las medida del reciclerView en agregarNuevoElemento, entoces si no esta vacio quiero que el
        //reciclerView tenga una dimensiones diferente para que se muestre bastante bien.
        if(!usuarios.isEmpty()){
            for (Usuario u : usuarios){
                itemList.add(new Item(u.getAvatarImg(), u.getNombreUsuario()));
            }
            redimensionarReciclerView();
        }
        // Agrega elementos ficticios a la lista
        itemList.add(new Item(R.drawable.icons8_m_s_50, "Crear"));
        // Agrega más elementos según sea necesario

        return itemList;
    }
    private void agregarNuevoElemento(String newData) {
        // Crea un nuevo objeto Item con los datos recibidos y agrégalo a la lista
        Item newItem = new Item(R.drawable.icons8_usuario_50, newData);
        itemList.add(0, newItem);

        // Notifica al adaptador que los datos han cambiado
        adapter.notifyDataSetChanged();
        redimensionarReciclerView();

    }
    public void redimensionarReciclerView(){
        float scale = getResources().getDisplayMetrics().density;
        int widthInDp = 270;
        int widthInPixels = (int) (widthInDp * scale + 0.5f);
        int marginTopInDp = 333;
        int marginTopInPixels = (int) (marginTopInDp * scale + 0.5f);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
        marginLayoutParams.topMargin = marginTopInPixels;

        // Configura el ancho y alto del RecyclerView
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.width = widthInPixels;
        int heightInDp = 384;
        int heightInPixels = (int) (heightInDp * scale + 0.5f);
        params.height = heightInPixels;

        // Aplica los cambios al RecyclerView
        recyclerView.setLayoutParams(params);
        recyclerView.setLayoutParams(marginLayoutParams);
    }



    public static void setUsuarioLogeado(Usuario usuarioLogeado) {
        MainActivityPrincipal.usuarioLogeado = usuarioLogeado;
    }

    public static void setUsuarios(List<Usuario> usuarios) {
        MainActivityPrincipal.usuarios = usuarios;
    }

    public static Usuario getUsuarioLogeado() {
        return usuarioLogeado;
    }

    public static List<Usuario> getUsuarios() {
        return usuarios;
    }
}