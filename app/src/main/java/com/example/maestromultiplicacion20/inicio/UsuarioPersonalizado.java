package com.example.maestromultiplicacion20.inicio;

/**
 * Clase para recrear el icono de la pantalla inicial
 */
public class UsuarioPersonalizado {
    private int imageResource;
    private String text;

    public UsuarioPersonalizado(int imageResource, String text) {
        this.imageResource = imageResource;
        this.text = text;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getText() {
        return text;
    }
}

