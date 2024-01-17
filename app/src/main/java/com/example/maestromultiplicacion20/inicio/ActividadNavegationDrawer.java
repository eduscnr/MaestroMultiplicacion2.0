package com.example.maestromultiplicacion20.inicio;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.database.EstadisticasDAOImpl;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.maestromultiplicacion20.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ActividadNavegationDrawer extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static int avatar;
    private static String dificultad = "Fácil";
    private static int tablaMultiplicar;
    private static int colorAplicacion;
    private static List<String> multiplicaciones;
    private static int indiceMultiplicacion;
    private static int tablaTemporalSeleccionada = -1;
    private static int indiceAvatar;
    private static List<Integer>avatares;
    private static List<Integer> avataresColeccionables;
    private static List<Integer> avataresFinales = new ArrayList<>(Arrays.asList(R.drawable.superman10, R.drawable.batman10, R.drawable.ironman10, R.drawable.spiderman10, R.drawable.thor10));
    private Menu menu;
    //Para guardas las estadisticas en la base de datos cuando le da para atras en el dispositivo o destruye la actividad
    private static int porcentajeExito = 100;
    private static int tablaSeleccionadoEnviar;
    private static List<String> multiplicacionesFallidas;
    private EstadisticasDAO estadisticasDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        multiplicaciones = new ArrayList<>();
        avatares = new ArrayList<>();
        avataresColeccionables = new ArrayList<>();
        multiplicacionesFallidas = new ArrayList<>();
        estadisticasDAO = new EstadisticasDAOImpl(this);
        setTablaMultiplicar(2);
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_configuracion, R.id.nav_entrenar, R.id.nav_estadisticas, R.id.nav_logros,
                R.id.nav_enviar, R.id.nav_contactos, R.id.nav_enviar, R.id.nav_salir, R.id.nav_eliminar)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.imageAvatar);
        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.textViewUsuario);
        textView.setText(ActividadPrincipal.getUsuarioLogeado().getNombreUsuario());
        imageView.setImageResource(ActividadPrincipal.getUsuarioLogeado().getAvatarImg());
        //Referencia de los id del menu para ocultar fragmentos, dependiendo del tipo de cuenta
        menu = navigationView.getMenu();
        MenuItem menuEstadisticas = menu.findItem(R.id.nav_estadisticas);
        MenuItem menuEntrenar = menu.findItem(R.id.nav_entrenar);
        MenuItem menConfiguracion = menu.findItem(R.id.nav_configuracion);
        MenuItem menuLogos = menu.findItem(R.id.nav_logros);
        MenuItem menuEnviarEsta = menu.findItem(R.id.nav_enviar);
        MenuItem menuContactos = menu.findItem(R.id.nav_contactos);
        MenuItem menuSalir = menu.findItem(R.id.nav_salir);
        MenuItem menuEliminar = menu.findItem(R.id.nav_eliminar);
        if(ActividadPrincipal.getUsuarioLogeado().getTipoCuenta().equalsIgnoreCase("usuario")){
            menuEstadisticas.setVisible(false);
            menuEnviarEsta.setVisible(false);
            menuContactos.setVisible(false);
            menuEliminar.setVisible(false);
            menuEntrenar.setVisible(true);
            menConfiguracion.setVisible(true);

            navController.navigate(R.id.nav_configuracion);
        }else{
            menuEstadisticas.setVisible(true);
            menuEnviarEsta.setVisible(true);
            menuContactos.setVisible(true);
            menuEliminar.setVisible(true);
            menuEntrenar.setVisible(false);
            menConfiguracion.setVisible(false);
            menuLogos.setVisible(false);
            navController.navigate(R.id.nav_estadisticas);
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        menuSalir.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                finish();
                return true;
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public static int getAvatar() {
        return avatar;
    }

    public static void setAvatar(int avatar) {
        ActividadNavegationDrawer.avatar = avatar;
    }

    public static String getDificultad() {
        return dificultad;
    }

    public static void setDificultad(String dificultad) {
        ActividadNavegationDrawer.dificultad = dificultad;
    }

    public static int getTablaMultiplicar() {
        return tablaMultiplicar;
    }

    public static void setTablaMultiplicar(int tablaMultiplicar) {
        ActividadNavegationDrawer.tablaMultiplicar = tablaMultiplicar;
    }

    public static int getColorAplicacion() {
        return colorAplicacion;
    }

    public static void setColorAplicacion(int colorAplicacion) {
        ActividadNavegationDrawer.colorAplicacion = colorAplicacion;
    }

    public static List<String> getMultiplicaciones() {
        return multiplicaciones;
    }

    public static int getIndiceMultiplicacion() {
        return indiceMultiplicacion;
    }

    public static void setIndiceMultiplicacion(int indiceMultiplicacion) {
        ActividadNavegationDrawer.indiceMultiplicacion = indiceMultiplicacion;
    }

    public static int getTablaTemporalSeleccionada() {
        return tablaTemporalSeleccionada;
    }

    public static void setTablaTemporalSeleccionada(int tablaTemporalSeleccionada) {
        ActividadNavegationDrawer.tablaTemporalSeleccionada = tablaTemporalSeleccionada;
    }

    public static List<Integer> getAvatares() {
        return avatares;
    }

    public static int getIndiceAvatar() {
        return indiceAvatar;
    }

    public static void setIndiceAvatar(int indiceAvatar) {
        ActividadNavegationDrawer.indiceAvatar = indiceAvatar;
    }

    public static List<Integer> getAvataresColeccionables() {
        return avataresColeccionables;
    }

    public static List<Integer> getAvataresFinales() {
        return avataresFinales;
    }
    public static String convertirFeche(GregorianCalendar calendar){
        return (calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1)
                + "/" + calendar.get(Calendar.YEAR));
    }

    public static void setPorcentajeExito(int porcentajeExito) {
        ActividadNavegationDrawer.porcentajeExito = porcentajeExito;
    }

    public static void setTablaSeleccionadoEnviar(int tablaSeleccionadoEnviar) {
        ActividadNavegationDrawer.tablaSeleccionadoEnviar = tablaSeleccionadoEnviar;
    }

    public static void setMultiplicacionesFallidas(List<String> multiplicacionesFallidas) {
        ActividadNavegationDrawer.multiplicacionesFallidas = multiplicacionesFallidas;
    }
    @Override
    protected void onDestroy() {
        if(indiceMultiplicacion != 0 && multiplicaciones.size() > 0 && !ActividadPrincipal.isEnviarEstadisticas()){
            if(multiplicacionesFallidas != null){
                String multiplicacionSinHacer = multiplicaciones.get(indiceMultiplicacion);
                multiplicacionesFallidas.add(multiplicacionSinHacer+"=Cambió");
                for (String multiplicacion : multiplicacionesFallidas){
                    porcentajeExito -= 10;
                }
            }
            estadisticasDAO.insertarEstadisticas(String.valueOf(porcentajeExito), String.valueOf(tablaMultiplicar), multiplicacionesFallidas,
                    estadisticasDAO.obtenerIdUsuario(ActividadPrincipal.getUsuarioLogeado().getNombreUsuario()), avatares.get(9));
            ActividadPrincipal.setEnviarEstadisticas(true);
        }
        super.onDestroy();
    }
}