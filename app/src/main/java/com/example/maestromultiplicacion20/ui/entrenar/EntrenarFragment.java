package com.example.maestromultiplicacion20.ui.entrenar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.maestromultiplicacion20.databinding.FragmentEntrenarBinding;
import com.example.maestromultiplicacion20.inicio.ActividadNavegationDrawer;
import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.example.maestromultiplicacion20.database.EstadisticasDAOImpl;
import com.example.maestromultiplicacion20.inicio.ActividadPrincipal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class EntrenarFragment extends Fragment implements View.OnClickListener{

    private FragmentEntrenarBinding binding;
    private TextView textViewMultiplicacion;
    private EditText editTextRespuesta;
    private TextView mostrarCorrecion;
    private TextView mostrarErroror;
    private ImageView mostrarIconoCorrecto;
    private ImageView mostrarIconoError;
    private ImageView imageViewAvatar;
    private Button botonValidar;
    private TextView procentaje;
    private ProgressBar barraProgreso;
    private int progreso;
    private int siguienteAvatar = 0;
    private int porcetajeDeExito = 100;
    private List<String> multiplicacionFallidas;
    private boolean enviarEstadisticas = true;
    private int tablaSeleccionadaEnviar = -1;
    private EstadisticasDAO estadisticasDAO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            ActividadNavegationDrawer.setIndiceMultiplicacion(savedInstanceState.getInt("indiceMultiplicacion"));
            multiplicacionFallidas = savedInstanceState.getStringArrayList("multiplicacionFallidas");
            porcetajeDeExito = savedInstanceState.getInt("porcentajeExito");
            progreso = savedInstanceState.getInt("progreso");
            enviarEstadisticas = savedInstanceState.getBoolean("enviarEstadisticas");
            tablaSeleccionadaEnviar = savedInstanceState.getInt("TablaSeleEnvi");
        }
        binding = FragmentEntrenarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        estadisticasDAO = new EstadisticasDAOImpl(requireContext());
        textViewMultiplicacion = root.findViewById(R.id.textViewMultiplicacion);
        editTextRespuesta = root.findViewById(R.id.editTextRespuesta);
        botonValidar = root.findViewById(R.id.btnEnviarRespuesta);
        mostrarCorrecion = root.findViewById(R.id.tvCorregir);
        mostrarIconoCorrecto = root.findViewById(R.id.ivCorrecion);
        mostrarIconoError = root.findViewById(R.id.ivError);
        imageViewAvatar = root.findViewById(R.id.ivAvatar);
        mostrarErroror = root.findViewById(R.id.tvError);
        procentaje = root.findViewById(R.id.tvPorcentaje);
        barraProgreso = root.findViewById(R.id.pbProgresoMulti);
        mostrarIconoCorrecto.setVisibility(View.GONE);
        mostrarIconoError.setVisibility(View.GONE);
        editTextRespuesta.setFocusable(false);
        aniadirBotones(11);
        botonValidar.setOnClickListener(this);
        botonValidar.setBackgroundColor(ActividadNavegationDrawer.getColorAplicacion());
        /*
         * Si el índice del avatar es mayor que cero debo de restar una foto porque en entrenar incremento uno y
         * cuando cambio entre fagmento se muestra un foto de más sin haber realizado la multiplicación
         * por donde se quedo
         * */
        if (ActividadNavegationDrawer.getTablaTemporalSeleccionada() == ActividadNavegationDrawer.getTablaMultiplicar()) {
            if (ActividadNavegationDrawer.getIndiceAvatar() > 0) {
                imageViewAvatar.setImageResource(ActividadNavegationDrawer.getAvatares().get(ActividadNavegationDrawer.getIndiceAvatar() - 1));
            }
        }
        if (ActividadNavegationDrawer.getTablaTemporalSeleccionada() != ActividadNavegationDrawer.getTablaMultiplicar()) {
            enviarEstadisticas = true;
            //Ingreso las estadisticas si el usuario ha cambia de fragmento.
            if(ActividadNavegationDrawer.getIndiceMultiplicacion() != 0 && ActividadNavegationDrawer.getIndiceMultiplicacion() != 10 && ActividadNavegationDrawer.getMultiplicaciones().size() > 0){
                String multiplicacionSinHacer = ActividadNavegationDrawer.getMultiplicaciones().get(ActividadNavegationDrawer.getIndiceMultiplicacion());
                multiplicacionFallidas.add(multiplicacionSinHacer+"=Cambio");
                estadisticasDAO.insertarEstadisticas(String.valueOf(porcetajeDeExito), String.valueOf(tablaSeleccionadaEnviar), multiplicacionFallidas, estadisticasDAO.obtenerIdUsuario(ActividadPrincipal.getUsuarioLogeado().getNombreUsuario()), ActividadNavegationDrawer.getAvatares().get(9));
            }
            inicializarAvatar(ActividadNavegationDrawer.getAvatar());
            entrenar(ActividadNavegationDrawer.getDificultad(), ActividadNavegationDrawer.getTablaMultiplicar());
        }
        if (ActividadNavegationDrawer.getMultiplicaciones().size() >0){
            mostrarSiguienteMultiplicacion();
        }
        //Muestro el porcentaje en un textView
        procentaje.setText(progreso + "%");
        return root;
    }
    @Override
    public void onClick(View view) {
        Button b;
        if (view.getId() == R.id.btnEnviarRespuesta) {
            validarRespuesta();
            editTextRespuesta.setText("");
        } else if (view.getClass().getSimpleName().equals("Button")) {
            b = (Button) view;
            if (b.getText().toString().equalsIgnoreCase("Borrar")) {
                if (editTextRespuesta.length() > 0) {
                    String borrando = editTextRespuesta.getText().toString().substring(0, editTextRespuesta.length() - 1);
                    editTextRespuesta.setText(borrando);
                }
            } else {
                if (ActividadNavegationDrawer.getIndiceMultiplicacion() < ActividadNavegationDrawer.getMultiplicaciones().size()) {
                    String textRespuesta = b.getText().toString();
                    editTextRespuesta.append(textRespuesta);
                }
            }
        }
    }

    /**
     * Método encargado de mostrar la siguiente multiplicación del la lista.
     */
    private void mostrarSiguienteMultiplicacion() {
        String multiplicacionActual;
        //Muestro la multiplicación si esta dentro del rango
        if (ActividadNavegationDrawer.getIndiceMultiplicacion() < ActividadNavegationDrawer.getMultiplicaciones().size()) {
            multiplicacionActual = ActividadNavegationDrawer.getMultiplicaciones().get(ActividadNavegationDrawer.getIndiceMultiplicacion());
            textViewMultiplicacion.setText(multiplicacionActual);
            /*Si esta fuera del rango significa que el indice a llegado al 10 y no hay más multiplicaciones lo que significa que hemos llegado al final
              y debo de mostrar la ultima multiplicación
            */
        } else {
            multiplicacionActual = ActividadNavegationDrawer.getMultiplicaciones().get(ActividadNavegationDrawer.getIndiceMultiplicacion() - 1);
            textViewMultiplicacion.setText(multiplicacionActual);
            //Si el boton de validar (Ok) habilitado envia la estadisticas.
            if(enviarEstadisticas && estadisticasDAO != null){
                estadisticasDAO.insertarEstadisticas(String.valueOf(porcetajeDeExito), String.valueOf(ActividadNavegationDrawer.getTablaMultiplicar()), multiplicacionFallidas, estadisticasDAO.obtenerIdUsuario(ActividadPrincipal.getUsuarioLogeado().getNombreUsuario()), ActividadNavegationDrawer.getAvatares().get(9));
                enviarEstadisticas = false;
                ActividadPrincipal.setEnviarEstadisticas(true);
            }
            //Si el indice del avatar a llegado al 10 porque hay 10 imagenes significa que a conseguido completar el avatar.
            if(ActividadNavegationDrawer.getIndiceAvatar() == 10){
                int avatar = ActividadNavegationDrawer.getAvatares().get(9);
                if(!ActividadNavegationDrawer.getAvataresColeccionables().contains(avatar)){
                    ActividadNavegationDrawer.getAvataresColeccionables().add(ActividadNavegationDrawer.getAvatares().get(9));
                }
            }
        }
    }

    /**
     * Método para validar la respuesta del usuario y verifica si es correcta o no, si es correcta incrementa el avatar.
     */
    private void validarRespuesta() {
        //Compruebo si el indice esta dentro de la lista,
        if (ActividadNavegationDrawer.getIndiceMultiplicacion() < ActividadNavegationDrawer.getMultiplicaciones().size()) {
            //Cojo la resulta del usuario
            String respuestaUsuario = editTextRespuesta.getText().toString();
            //Uso un delimitador para coger el primer operador y el segundo
            String multiplicadores[] = ActividadNavegationDrawer.getMultiplicaciones().get(ActividadNavegationDrawer.getIndiceMultiplicacion()).split("x");
            //Una vez delimitado realiza la operación
            int respuesta = Integer.parseInt(multiplicadores[0].trim()) * Integer.parseInt(multiplicadores[1].trim());

            barraProgreso.setProgress(barraProgreso.getProgress() + 10);
            progreso += 10;
            procentaje.setText(progreso + "%");
            //Compruebo si la respues es correcta, si lo es muestro la mutiplicación como correcta y su tick, también incremento el avatar
            if (String.valueOf(respuesta).equalsIgnoreCase(respuestaUsuario)) {
                mostrarIconoCorrecto.setVisibility(View.VISIBLE);
                mostrarErroror.setText("");
                mostrarIconoError.setVisibility(View.GONE);
                mostrarCorrecion.setText(ActividadNavegationDrawer.getMultiplicaciones().get(ActividadNavegationDrawer.getIndiceMultiplicacion()) + "=" + respuesta);
                imageViewAvatar.setImageResource(ActividadNavegationDrawer.getAvatares().get(ActividadNavegationDrawer.getIndiceAvatar()));
                ActividadNavegationDrawer.setIndiceAvatar(ActividadNavegationDrawer.getIndiceAvatar() + 1);
                //Si es incorrectao muestro el resultado mal con un cruz y el otro textView el resultado correcto con tick
            } else {
                mostrarIconoError.setVisibility(View.VISIBLE);
                mostrarIconoCorrecto.setVisibility(View.VISIBLE);
                mostrarCorrecion.setText(ActividadNavegationDrawer.getMultiplicaciones().get(ActividadNavegationDrawer.getIndiceMultiplicacion()) + "=" + respuesta);
                mostrarErroror.setText(ActividadNavegationDrawer.getMultiplicaciones().get(ActividadNavegationDrawer.getIndiceMultiplicacion()) + "=" + respuestaUsuario);
                porcetajeDeExito -= 10;
                multiplicacionFallidas.add(ActividadNavegationDrawer.getMultiplicaciones().get(ActividadNavegationDrawer.getIndiceMultiplicacion()) + "=" + respuestaUsuario);
            }
            //Incremento el indice de la multiplicación
            ActividadNavegationDrawer.setIndiceMultiplicacion(ActividadNavegationDrawer.getIndiceMultiplicacion() + 1);
            mostrarSiguienteMultiplicacion();
        }
    }

    /**
     * Método pare generar en tiempo de ejecución botones en el GridPanel
     * @param i
     */
    private void aniadirBotones(int i) {
        GridLayout g = (GridLayout) binding.getRoot().findViewById(R.id.gridBotonera);
        Button b;
        for (int j = 0; j < i; j++) {
            b = new Button(this.getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(8, 8, 8, 8);
            b.setLayoutParams(params);
            if (j == i - 1) {
                b.setText("Borrar");

            } else if (j == i - 2) {
                b.setText("0");
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 2);
                params.setGravity(Gravity.FILL_HORIZONTAL);
            } else {
                b.setText(String.valueOf(j + 1));
            }
            b.setId(j);
            b.setOnClickListener(this);
            b.setBackgroundColor(ActividadNavegationDrawer.getColorAplicacion());
            b.setTextColor(Color.WHITE);
            g.addView(b, j);
        }
    }

    /**
     * Método para recoger la tabla de multiplicar que me pasan por parametro (int tabla) y su dificultad.
     * @param dificultad dificulta de esa tabla
     * @param tabla el número de la tabla
     */
    private void entrenar(String dificultad, int tabla) {
        ActividadNavegationDrawer.getMultiplicaciones().clear();
        ActividadNavegationDrawer.setIndiceMultiplicacion(0);
        ActividadPrincipal.setEnviarEstadisticas(false);
        switch (dificultad) {
            case "Fácil":
                for (int i = 1; i <= 10; i++) {
                    ActividadNavegationDrawer.getMultiplicaciones().add(tabla + "x" + String.valueOf(i));
                }
                break;
            case "Media":
                for (int i = 10; i >= 1; i--) {
                    ActividadNavegationDrawer.getMultiplicaciones().add(tabla + "x" + String.valueOf(i));
                }
                break;
            case "Dificil":
                for (int i = 1; i <= 10; i++) {
                    ActividadNavegationDrawer.getMultiplicaciones().add(tabla + "x" + String.valueOf(i));
                }
                Collections.shuffle(ActividadNavegationDrawer.getMultiplicaciones());
                break;
        }
        porcetajeDeExito = 100;
        barraProgreso.setIndeterminate(false);
        procentaje.setText("0%");
        progreso = 0;
        barraProgreso.postDelayed(new Runnable() {
            @Override
            public void run() {
                barraProgreso.setProgress(0);
            }
        }, 100);
    }

    /**
     * Método para recoger las imagenes del avatar que ha seleccionado.
     * @param posicionAvatar índice de esa avatar para combiar de color
     */
    private void inicializarAvatar(int posicionAvatar) {
        ActividadNavegationDrawer.getAvatares().clear();
        multiplicacionFallidas = new ArrayList<>();
        ActividadPrincipal.setEnviarEstadisticas(false);
        ActividadNavegationDrawer.setIndiceAvatar(0);
        switch (posicionAvatar) {
            case 0:
                ActividadNavegationDrawer.getAvatares().addAll(Arrays.asList(R.drawable.superman01, R.drawable.superman02, R.drawable.superman03, R.drawable.superman04, R.drawable.superman05,
                        R.drawable.superman06, R.drawable.superman07, R.drawable.superman08, R.drawable.superman09, R.drawable.superman10));
                break;
            case 1:
                ActividadNavegationDrawer.getAvatares().addAll(Arrays.asList(R.drawable.batman01, R.drawable.batman02, R.drawable.batman03, R.drawable.batman04, R.drawable.batman05,
                        R.drawable.batman06, R.drawable.batman07, R.drawable.batman08, R.drawable.batman09, R.drawable.batman10));
                break;
            case 2:
                ActividadNavegationDrawer.getAvatares().addAll(Arrays.asList(R.drawable.ironman01, R.drawable.ironman02, R.drawable.ironman03, R.drawable.ironman04, R.drawable.ironman05,
                        R.drawable.ironman06, R.drawable.ironman07, R.drawable.ironman08, R.drawable.ironman09, R.drawable.ironman10));
                break;
            case 3:
                ActividadNavegationDrawer.getAvatares().addAll(Arrays.asList(R.drawable.spiderman01, R.drawable.spiderman02, R.drawable.spiderman03, R.drawable.spiderman04, R.drawable.spiderman05,
                        R.drawable.spiderman06, R.drawable.spiderman07, R.drawable.spiderman08, R.drawable.spiderman09, R.drawable.spiderman10));
                break;
            case 4:
                ActividadNavegationDrawer.getAvatares().addAll(Arrays.asList(R.drawable.thor01, R.drawable.thor02, R.drawable.thor03, R.drawable.thor04, R.drawable.thor05,
                        R.drawable.thor06, R.drawable.thor07, R.drawable.thor08, R.drawable.thor09, R.drawable.thor10));
                break;
            default:
                System.out.println("No hay mas avatares");
                break;
        }
    }
    //Esto se ejecuta antes que el onSaveInstanceState, ciclo de vida del fragment
    @Override
    public void onStop() {
        super.onStop();
        editTextRespuesta.setText("");
        //Guardo este valor cuando el usuario a cambiado de tabla,
        //si no ha terminado la tabla me sirve para guardarlo en la base de datos.
        tablaSeleccionadaEnviar = ActividadNavegationDrawer.getTablaMultiplicar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("multiplicacionFallidas", (ArrayList<String>) multiplicacionFallidas);
        outState.putInt("indiceMultiplicacion", ActividadNavegationDrawer.getIndiceMultiplicacion());
        outState.putInt("porcentajeExito", porcetajeDeExito);
        outState.putInt("progreso", progreso);
        outState.putBoolean("enviarEstadisticas", enviarEstadisticas);
        outState.putInt("TablaSeleEnvi", tablaSeleccionadaEnviar);
    }
    @Override
    public void onDestroyView() {
        ActividadNavegationDrawer.setTablaTemporalSeleccionada(ActividadNavegationDrawer.getTablaMultiplicar());
        ActividadNavegationDrawer.setPorcentajeExito(porcetajeDeExito);
        ActividadNavegationDrawer.setMultiplicacionesFallidas(multiplicacionFallidas);
        ActividadNavegationDrawer.setTablaSeleccionadoEnviar(tablaSeleccionadaEnviar);
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        ActividadNavegationDrawer.setTablaTemporalSeleccionada(ActividadNavegationDrawer.getTablaMultiplicar());
        ActividadNavegationDrawer.setPorcentajeExito(porcetajeDeExito);
        ActividadNavegationDrawer.setMultiplicacionesFallidas(multiplicacionFallidas);
        ActividadNavegationDrawer.setTablaSeleccionadoEnviar(tablaSeleccionadaEnviar);
        //Envio tambien los resultados a la ActividadPrincipal
        ActividadPrincipal.setAvatarJugado(ActividadNavegationDrawer.getAvatares().get(9));
        ActividadPrincipal.setMultiplicacionesFallidas(multiplicacionFallidas);
        ActividadPrincipal.setTablaSeleccionada(ActividadNavegationDrawer.getTablaMultiplicar() == -1 ? 2 : ActividadNavegationDrawer.getTablaMultiplicar());
        ActividadPrincipal.setIndiceMultiplicacion(ActividadNavegationDrawer.getIndiceMultiplicacion());
        super.onPause();
    }
}