package com.example.maestromultiplicacion20.inicio;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.adaptadores.UsuarioPersonalizadoAdapter;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.example.maestromultiplicacion20.database.EstadisticasDAOImpl;
import com.example.maestromultiplicacion20.database.Sqlite;
import com.example.maestromultiplicacion20.modelo.Usuario;
import com.example.maestromultiplicacion20.modelo.UsuarioPersonalizado;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal, es lo que se muestra el principio de la app
 */
public class ActividadPrincipal extends AppCompatActivity {
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private List<UsuarioPersonalizado> itemList;
    private UsuarioPersonalizadoAdapter adapter;
    private EstadisticasDAO estadisticasDAO;
    private RecyclerView recyclerView;
    private static List<Usuario> usuarios;
    private static Usuario usuarioLogeado;
    //Variable para enviar estadisticas cuando el servicio a sido destruido, es decir, cuando se a cerrado la aplicación
    private static List<String> multiplicacionesFallidas;
    private static int porcentajeExito = 100;
    private static int tablaSeleccionada;
    private static int avatarJugado;
    private static int indiceMultiplicacion;
    private static boolean enviarEstadisticas = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_principal);
        Sqlite sqlite = new Sqlite(this);
        estadisticasDAO = new EstadisticasDAOImpl(this);
        sqlite.getWritableDatabase();
        //Obtiene una lista de usuarios que existe en la base de datos
        usuarios = estadisticasDAO.obtenerUsuarios();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //Añado una personalizado a los usuarios existentes
        itemList = obtenerDatos();
        adapter = new UsuarioPersonalizadoAdapter(itemList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        //Reglas para que no afecte el modo oscuro y que no se pueda poner horizontal
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Añado un onClick en el adaptador para elegir el usuario que quiere iniciar en la aplicación, este onClick seria una interfaz
        adapter.setOnItemClickListener(new UsuarioPersonalizadoAdapter.OnItemClickListener() {
            @Override
            public void onItemButtonClick() {
                // Hago un intent que ejecuta otra actividad que seria la de login
                Intent intent = new Intent(ActividadPrincipal.this, ActividadSignUp.class);
                activityResultLauncher.launch(intent);
            }
        });
        //Recoge el resultado y añado un nuevo usuario si ha salido todo con exito
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK)  {
                        Intent data = result.getData();
                        if (data != null) {
                            String usuario = data.getStringExtra("Usuario");
                            agregarNuevoElemento(usuario);
                            //Si la lista ha excedido su limete máximo, quito el boton de agregar/añadir
                            if(itemList.size() >= 6){
                                itemList.remove(itemList.size()-1);
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }
                }
        );
    }

    /**
     * Método para recopilar usuario existentes en la base de datos
     * @return devuelvo una lista de UsuariosPersonalizado que seria un avatar/imagen y un texto que seria el nombre de usuario
     */
    private List<UsuarioPersonalizado> obtenerDatos() {
        List<UsuarioPersonalizado> itemList = new ArrayList<>();
        //Uso esto porque juego con las medida del reciclerView en agregarNuevoElemento, entoces si no esta vacio quiero que el
        //reciclerView tenga una dimensiones diferente para que se muestre bastante bien.
        if(!usuarios.isEmpty()){
            for (Usuario u : usuarios){
                itemList.add(new UsuarioPersonalizado(u.getAvatarImg(), u.getNombreUsuario()));
            }
            redimensionarReciclerView();
        }
        //Añado el elemento crear si lo ha excedido su lime
        if(itemList.size() < 5){
            itemList.add(new UsuarioPersonalizado(R.drawable.icons8_m_s_50, "Crear"));
        }

        return itemList;
    }

    /**
     * Método para crear nuevos usuarios, ya sea administracidor o usuario normal
     * @param nombreUsuario paso por paramento el nombre de usuario para crear su Login
     */
    private void agregarNuevoElemento(String nombreUsuario) {
        if(itemList.size() <= 5){
            UsuarioPersonalizado newItem = new UsuarioPersonalizado(R.drawable.icons8_usuario_48__1_, nombreUsuario);
            itemList.add(0, newItem);

            adapter.notifyDataSetChanged();
            redimensionarReciclerView();
        }
    }

    /**
     * Método para redimensionar el RecyclerView, cuando existe un solo elemento ("Crear") debe de esta en el centro para
     * que quede más estético,cuando se añade un nuevo elemento el RecyclerView se redimensiona para que queda más estético
     */
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
        ActividadPrincipal.usuarioLogeado = usuarioLogeado;
    }

    public static void setUsuarios(List<Usuario> usuarios) {
        ActividadPrincipal.usuarios = usuarios;
    }

    public static Usuario getUsuarioLogeado() {
        return usuarioLogeado;
    }

    public static List<Usuario> getUsuarios() {
        return usuarios;
    }

    public static boolean isEnviarEstadisticas() {
        return enviarEstadisticas;
    }

    public static void setEnviarEstadisticas(boolean enviarEstadisticas) {
        ActividadPrincipal.enviarEstadisticas = enviarEstadisticas;
    }

    public static List<String> getMultiplicacionesFallidas() {
        return multiplicacionesFallidas;
    }

    public static void setMultiplicacionesFallidas(List<String> multiplicacionesFallidas) {
        ActividadPrincipal.multiplicacionesFallidas = multiplicacionesFallidas;
    }

    public static int getPorcentajeExito() {
        return porcentajeExito;
    }

    public static void setPorcentajeExito(int porcentajeExito) {
        ActividadPrincipal.porcentajeExito = porcentajeExito;
    }

    public static int getTablaSeleccionada() {
        return tablaSeleccionada;
    }

    public static void setTablaSeleccionada(int tablaSeleccionada) {
        ActividadPrincipal.tablaSeleccionada = tablaSeleccionada;
    }

    public static int getAvatarJugado() {
        return avatarJugado;
    }

    public static void setAvatarJugado(int avatarJugado) {
        ActividadPrincipal.avatarJugado = avatarJugado;
    }

    public static void setIndiceMultiplicacion(int indiceMultiplicacion) {
        ActividadPrincipal.indiceMultiplicacion = indiceMultiplicacion;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        usuarios = estadisticasDAO.obtenerUsuarios();
        itemList = obtenerDatos();
        adapter.setItemList(itemList);
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(!enviarEstadisticas && indiceMultiplicacion != 0 && indiceMultiplicacion != 10){
            if(multiplicacionesFallidas != null){
                for (String mutiplicacion : multiplicacionesFallidas){
                    porcentajeExito -= 10;
                }
            }
            estadisticasDAO.insertarEstadisticas(String.valueOf(porcentajeExito), String.valueOf(tablaSeleccionada), multiplicacionesFallidas,
                    estadisticasDAO.obtenerIdUsuario(ActividadPrincipal.getUsuarioLogeado().getNombreUsuario()), ActividadNavegationDrawer.getAvatares().get(9));
        }
        super.onDestroy();
    }
}