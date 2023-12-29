package com.example.maestromultiplicacion20.ui.entrenar;

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

import com.example.maestromultiplicacion20.MainActivity;
import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.interfaces.EstadisticasDAO;
import com.example.maestromultiplicacion20.database.EstadisticasDAOImpl;
import com.example.maestromultiplicacion20.databinding.FragmentEntrenarBinding;
import com.example.maestromultiplicacion20.inicio.MainActivityPrincipal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * FALTA:
 * -CUANDO TERMINE LA TABLA DEBERIA DE GUARDAR LAS ESTADISTICAS EN LA BASE DE DATOS
 * -BUSCAR CON QUE USUARIO ESTA LOGEADO Y GUARDA ESAS ESTADISTICAS A ESE USUARIO
 * -CUANDO SE VAYA DEL FRAGMENTO Y CAMBIE DE MULTIPLICACION DEBERIA PONER EN EL RESULTADO DE DONDE SE HA QUEDA "ha cambiado de multiplicacion" o "tabla cambiada"
 */
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
            MainActivity.setIndiceMultiplicacion(savedInstanceState.getInt("indiceMultiplicacion"));
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
        botonValidar.setBackgroundColor(MainActivity.getColorAplicacion());
        /*
         * Si el índice del avatar es mayor que cero debo de restar una foto porque en entrenar incremento uno y
         * cuando cambio entre fagmento se muestra un foto de más sin haber realizado la multiplicación
         * por donde se quedo
         * */
        if (MainActivity.getTablaTemporalSeleccionada() == MainActivity.getTablaMultiplicar()) {
            if (MainActivity.getIndiceAvatar() > 0) {
                imageViewAvatar.setImageResource(MainActivity.getAvatares().get(MainActivity.getIndiceAvatar() - 1));
            }
        }
        if (MainActivity.getTablaTemporalSeleccionada() != MainActivity.getTablaMultiplicar()) {
            enviarEstadisticas = true;
            //Ingreso las estadisticas si el usuario ha cambia de fragmento.
            if(MainActivity.getIndiceMultiplicacion() != 0 && MainActivity.getIndiceMultiplicacion() != 10){
                String multiplicacionSinHacer = MainActivity.getMultiplicaciones().get(MainActivity.getIndiceMultiplicacion());
                System.out.println("Aqui me he quedado: " + multiplicacionSinHacer);
                multiplicacionFallidas.add(multiplicacionSinHacer+"=cambio de tabla");
                estadisticasDAO.insertarEstadisticas(String.valueOf(porcetajeDeExito), String.valueOf(tablaSeleccionadaEnviar), multiplicacionFallidas, estadisticasDAO.obtenerIdUsuario(MainActivityPrincipal.getUsuarioLogeado().getNombreUsuario()), MainActivity.getAvatares().get(9));
            }
            inicializarAvatar(MainActivity.getAvatar());
            entrenar(MainActivity.getDificultad(), MainActivity.getTablaMultiplicar());
        }
        mostrarSiguienteMultiplicacion();
        //Muestro el porcentaje en un textView
        procentaje.setText(progreso + "%");
        return root;
    }

    @Override
    public void onDestroyView() {
        MainActivity.setTablaTemporalSeleccionada(MainActivity.getTablaMultiplicar());
        super.onDestroyView();
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
                if (MainActivity.getIndiceMultiplicacion() < MainActivity.getMultiplicaciones().size()) {
                    String textRespuesta = b.getText().toString();
                    editTextRespuesta.append(textRespuesta);
                }
            }
        }
    }

    //Método encargado de mostrar la siguiente multiplicación del la lista.
    private void mostrarSiguienteMultiplicacion() {
        String multiplicacionActual;
        //Muestro la multiplicación si esta dentro del rango
        if (MainActivity.getIndiceMultiplicacion() < MainActivity.getMultiplicaciones().size()) {
            multiplicacionActual = MainActivity.getMultiplicaciones().get(MainActivity.getIndiceMultiplicacion());
            textViewMultiplicacion.setText(multiplicacionActual);
            /*Si esta fuera del rango significa que el indice a llegado al 10 y no hay más multiplicaciones lo que significa que hemos llegado al final
              y debo de mostrar la ultima multiplicación
            */
        } else {
            multiplicacionActual = MainActivity.getMultiplicaciones().get(MainActivity.getIndiceMultiplicacion() - 1);
            textViewMultiplicacion.setText(multiplicacionActual);
            //Si el boton de validar (Ok) habilitado envia la estadisticas.
            if(enviarEstadisticas){
                estadisticasDAO.insertarEstadisticas(String.valueOf(porcetajeDeExito), String.valueOf(MainActivity.getTablaMultiplicar()), multiplicacionFallidas, estadisticasDAO.obtenerIdUsuario(MainActivityPrincipal.getUsuarioLogeado().getNombreUsuario()), MainActivity.getAvatares().get(9));
                enviarEstadisticas = false;
            }
            //Si el indice del avatar a llegado al 10 porque hay 10 imagenes significa que a conseguido completar el avatar.
            if(MainActivity.getIndiceAvatar() == 10){
                int avatar = MainActivity.getAvatares().get(9);
                if(!MainActivity.getAvataresColeccionables().contains(avatar)){
                    MainActivity.getAvataresColeccionables().add(MainActivity.getAvatares().get(9));
                }
            }
        }
    }

    //Método para validar la respuesta del usuario y verifica si es correcta o no, si es correcta incrementa el avatar.
    private void validarRespuesta() {
        //Compruebo si el indice esta dentro de la lista,
        if (MainActivity.getIndiceMultiplicacion() < MainActivity.getMultiplicaciones().size()) {
            //Cojo la resulta del usuario
            String respuestaUsuario = editTextRespuesta.getText().toString();
            //Uso un delimitador para coger el primer operador y el segundo
            String multiplicadores[] = MainActivity.getMultiplicaciones().get(MainActivity.getIndiceMultiplicacion()).split("x");
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
                mostrarCorrecion.setText(MainActivity.getMultiplicaciones().get(MainActivity.getIndiceMultiplicacion()) + "=" + respuesta);
                imageViewAvatar.setImageResource(MainActivity.getAvatares().get(MainActivity.getIndiceAvatar()));
                MainActivity.setIndiceAvatar(MainActivity.getIndiceAvatar() + 1);
                //Si es incorrectao muestro el resultado mal con un cruz y el otro textView el resultado correcto con tick
            } else {
                mostrarIconoError.setVisibility(View.VISIBLE);
                mostrarIconoCorrecto.setVisibility(View.VISIBLE);
                mostrarCorrecion.setText(MainActivity.getMultiplicaciones().get(MainActivity.getIndiceMultiplicacion()) + "=" + respuesta);
                mostrarErroror.setText(MainActivity.getMultiplicaciones().get(MainActivity.getIndiceMultiplicacion()) + "=" + respuestaUsuario);
                porcetajeDeExito -= 10;
                multiplicacionFallidas.add(MainActivity.getMultiplicaciones().get(MainActivity.getIndiceMultiplicacion()) + "=" + respuestaUsuario);
            }
            //Incremento el indice de la multiplicación
            MainActivity.setIndiceMultiplicacion(MainActivity.getIndiceMultiplicacion() + 1);
            mostrarSiguienteMultiplicacion();
        }
    }

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
            b.setBackgroundColor(MainActivity.getColorAplicacion());
            b.setTextColor(Color.WHITE);
            g.addView(b, j);
        }
    }

    //Método para recoger la tabla de multiplicar que me pasan por parametro (int tabla) y su dificultad.
    private void entrenar(String dificultad, int tabla) {
        MainActivity.getMultiplicaciones().clear();
        MainActivity.setIndiceMultiplicacion(0);
        switch (dificultad) {
            case "Fácil":
                for (int i = 1; i <= 10; i++) {
                    MainActivity.getMultiplicaciones().add(tabla + "x" + String.valueOf(i));
                }
                break;
            case "Media":
                for (int i = 10; i >= 1; i--) {
                    MainActivity.getMultiplicaciones().add(tabla + "x" + String.valueOf(i));
                }
                break;
            case "Dificil":
                for (int i = 1; i <= 10; i++) {
                    MainActivity.getMultiplicaciones().add(tabla + "x" + String.valueOf(i));
                }
                Collections.shuffle(MainActivity.getMultiplicaciones());
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

    //Método para recoger las imagenes del avatar que ha seleccionado.
    private void inicializarAvatar(int posicionAvatar) {
        MainActivity.getAvatares().clear();
        multiplicacionFallidas = new ArrayList<>();
        MainActivity.setIndiceAvatar(0);
        switch (posicionAvatar) {
            case 0:
                MainActivity.getAvatares().addAll(Arrays.asList(R.drawable.superman01, R.drawable.superman02, R.drawable.superman03, R.drawable.superman04, R.drawable.superman05,
                        R.drawable.superman06, R.drawable.superman07, R.drawable.superman08, R.drawable.superman09, R.drawable.superman10));
                break;
            case 1:
                MainActivity.getAvatares().addAll(Arrays.asList(R.drawable.batman01, R.drawable.batman02, R.drawable.batman03, R.drawable.batman04, R.drawable.batman05,
                        R.drawable.batman06, R.drawable.batman07, R.drawable.batman08, R.drawable.batman09, R.drawable.batman10));
                break;
            case 2:
                MainActivity.getAvatares().addAll(Arrays.asList(R.drawable.ironman01, R.drawable.ironman02, R.drawable.ironman03, R.drawable.ironman04, R.drawable.ironman05,
                        R.drawable.ironman06, R.drawable.ironman07, R.drawable.ironman08, R.drawable.ironman09, R.drawable.ironman10));
                break;
            case 3:
                MainActivity.getAvatares().addAll(Arrays.asList(R.drawable.spiderman01, R.drawable.spiderman02, R.drawable.spiderman03, R.drawable.spiderman04, R.drawable.spiderman05,
                        R.drawable.spiderman06, R.drawable.spiderman07, R.drawable.spiderman08, R.drawable.spiderman09, R.drawable.spiderman10));
                break;
            case 4:
                MainActivity.getAvatares().addAll(Arrays.asList(R.drawable.thor01, R.drawable.thor02, R.drawable.thor03, R.drawable.thor04, R.drawable.thor05,
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
        tablaSeleccionadaEnviar = MainActivity.getTablaMultiplicar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("multiplicacionFallidas", (ArrayList<String>) multiplicacionFallidas);
        outState.putInt("indiceMultiplicacion", MainActivity.getIndiceMultiplicacion());
        outState.putInt("porcentajeExito", porcetajeDeExito);
        outState.putInt("progreso", progreso);
        outState.putBoolean("enviarEstadisticas", enviarEstadisticas);
        outState.putInt("TablaSeleEnvi", tablaSeleccionadaEnviar);
    }
}