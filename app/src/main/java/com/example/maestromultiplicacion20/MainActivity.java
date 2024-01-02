package com.example.maestromultiplicacion20;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maestromultiplicacion20.inicio.MainActivityPrincipal;
import com.example.maestromultiplicacion20.modelo.Estadisticas;
import com.example.maestromultiplicacion20.ui.logros.FragmentLogros;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
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

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static int avatar;
    private static String dificultad = "Fácil";
    private static int tablaMultiplicar;
    private static int colorAplicacion;
    private static List<String> multiplicaciones;
    private static int indiceMultiplicacion;
    private static int tablaTemporalSeleccionada;
    private static int indiceAvatar;
    private static List<Integer>avatares;
    private static List<Estadisticas> estadisticas;
    private static List<Integer> avataresColeccionables;
    private static List<Integer> avataresFinales = new ArrayList<>(Arrays.asList(R.drawable.superman10, R.drawable.batman10, R.drawable.ironman10, R.drawable.spiderman10, R.drawable.thor10));
    private static GregorianCalendar horario;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        multiplicaciones = new ArrayList<>();
        avatares = new ArrayList<>();
        estadisticas = new ArrayList<>();
        avataresColeccionables = new ArrayList<>();
        setTablaMultiplicar(2);
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_configuracion, R.id.nav_entrenar, R.id.nav_estadisticas, R.id.nav_logros, R.id.nav_enviar, R.id.nav_contactos, R.id.nav_contactosFavoritos)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.imageAvatar);
        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.textViewUsuario);
        textView.setText(MainActivityPrincipal.getUsuarioLogeado().getNombreUsuario());
        imageView.setImageResource(MainActivityPrincipal.getUsuarioLogeado().getAvatarImg());
        //Referencia de los id del menu para ocultar fragmentos, dependiendo del tipo de cuenta
        menu = navigationView.getMenu();
        MenuItem menuEstadisticas = menu.findItem(R.id.nav_estadisticas);
        MenuItem menuEntrenar = menu.findItem(R.id.nav_entrenar);
        MenuItem menConfiguracion = menu.findItem(R.id.nav_configuracion);
        MenuItem menuLogos = menu.findItem(R.id.nav_logros);
        MenuItem menuEnviarEsta = menu.findItem(R.id.nav_enviar);
        MenuItem menuContactos = menu.findItem(R.id.nav_contactos);
        MenuItem menuContactosFav = menu.findItem(R.id.nav_contactosFavoritos);
        if(MainActivityPrincipal.getUsuarioLogeado().getTipoCuenta().equalsIgnoreCase("usuario")){
            menuEstadisticas.setVisible(false);
            menuEnviarEsta.setVisible(false);
            menuContactos.setVisible(false);
            menuContactosFav.setVisible(false);
            menuEntrenar.setVisible(true);
            menConfiguracion.setVisible(true);

            navController.navigate(R.id.nav_configuracion);
        }else{
            menuEstadisticas.setVisible(true);
            menuEnviarEsta.setVisible(true);
            menuContactos.setVisible(true);
            menuContactosFav.setVisible(true);
            menuEntrenar.setVisible(false);
            menConfiguracion.setVisible(false);
            menuLogos.setVisible(false);
            navController.navigate(R.id.nav_estadisticas);
        }
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
        MainActivity.avatar = avatar;
    }

    public static String getDificultad() {
        return dificultad;
    }

    public static void setDificultad(String dificultad) {
        MainActivity.dificultad = dificultad;
    }

    public static int getTablaMultiplicar() {
        return tablaMultiplicar;
    }

    public static void setTablaMultiplicar(int tablaMultiplicar) {
        MainActivity.tablaMultiplicar = tablaMultiplicar;
    }

    public static int getColorAplicacion() {
        return colorAplicacion;
    }

    public static void setColorAplicacion(int colorAplicacion) {
        MainActivity.colorAplicacion = colorAplicacion;
    }

    public static List<String> getMultiplicaciones() {
        return multiplicaciones;
    }

    public static void setMultiplicaciones(List<String> multiplicaciones) {
        MainActivity.multiplicaciones = multiplicaciones;
    }

    public static int getIndiceMultiplicacion() {
        return indiceMultiplicacion;
    }

    public static void setIndiceMultiplicacion(int indiceMultiplicacion) {
        MainActivity.indiceMultiplicacion = indiceMultiplicacion;
    }

    public static int getTablaTemporalSeleccionada() {
        return tablaTemporalSeleccionada;
    }

    public static void setTablaTemporalSeleccionada(int tablaTemporalSeleccionada) {
        MainActivity.tablaTemporalSeleccionada = tablaTemporalSeleccionada;
    }

    public static List<Integer> getAvatares() {
        return avatares;
    }

    public static void setAvatares(List<Integer> avatares) {
        MainActivity.avatares = avatares;
    }

    public static int getIndiceAvatar() {
        return indiceAvatar;
    }

    public static void setIndiceAvatar(int indiceAvatar) {
        MainActivity.indiceAvatar = indiceAvatar;
    }

    public static List<Estadisticas> getEstadisticas() {
        return estadisticas;
    }

    public static List<Integer> getAvataresColeccionables() {
        return avataresColeccionables;
    }

    public static List<Integer> getAvataresFinales() {
        return avataresFinales;
    }

    public static void setAvataresFinales(List<Integer> avataresFinales) {
        MainActivity.avataresFinales = avataresFinales;
    }

    public static GregorianCalendar getHorario() {
        return horario;
    }
    public static String convertirFeche(GregorianCalendar calendar){
        return (calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1)
                + "/" + calendar.get(Calendar.YEAR));
    }
}