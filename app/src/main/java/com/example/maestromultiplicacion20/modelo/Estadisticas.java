package com.example.maestromultiplicacion20.modelo;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Clase para guardas las estadísticas
 */
public class Estadisticas {
    private int tablaSeleccionada;
    private String porcetajeDeExito;
    private List<String> multiplicacionesFallidas;
    private GregorianCalendar fecha;
    private int avatarSeleccionado;

    public Estadisticas() {
    }

    public Estadisticas(int tablaSeleccionada, String porcetajeDeExito, List<String> multiplicacionesFallidas, GregorianCalendar fecha, int avatarSeleccionado) {
        this.tablaSeleccionada = tablaSeleccionada;
        this.porcetajeDeExito = porcetajeDeExito;
        this.multiplicacionesFallidas = multiplicacionesFallidas;
        this.fecha = fecha;
        this.avatarSeleccionado = avatarSeleccionado;
    }

    public Estadisticas(int tablaSeleccionada, String porcetajeDeExito, List<String> multiplicacionesFallidas) {
        this.tablaSeleccionada = tablaSeleccionada;
        this.porcetajeDeExito = porcetajeDeExito;
        this.multiplicacionesFallidas = multiplicacionesFallidas;
    }

    public Estadisticas(int tablaSeleccionada, String porcetajeDeExito, List<String> multiplicacionesFallidas, GregorianCalendar fecha) {
        this.tablaSeleccionada = tablaSeleccionada;
        this.porcetajeDeExito = porcetajeDeExito;
        this.multiplicacionesFallidas = multiplicacionesFallidas;
        this.fecha = fecha;
    }

    public int getTablaSeleccionada() {
        return tablaSeleccionada;
    }

    public void setTablaSeleccionada(int tablaSeleccionada) {
        this.tablaSeleccionada = tablaSeleccionada;
    }

    public String getPorcetajeDeExito() {
        return porcetajeDeExito;
    }

    public void setPorcetajeDeExito(String porcetajeDeExito) {
        this.porcetajeDeExito = porcetajeDeExito;
    }

    public List<String> getMultiplicacionesFallidas() {
        return multiplicacionesFallidas;
    }

    public void setMultiplicacionesFallidas(List<String> multiplicacionesFallidas) {
        this.multiplicacionesFallidas = multiplicacionesFallidas;
    }

    public GregorianCalendar getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return "Estadisticas{" +
                "tablaSeleccionada=" + tablaSeleccionada +
                ", porcetajeDeExito='" + porcetajeDeExito + '\'' +
                ", multiplicacionesFallidas=" + multiplicacionesFallidas +
                '}';
    }

    public String convertirFecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha.getTime());
    }

    public int getAvatarSeleccionado() {
        return avatarSeleccionado;
    }
}
