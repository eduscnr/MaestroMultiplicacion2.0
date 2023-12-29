package com.example.maestromultiplicacion20.modelo;

public class Contacto {
    private String nombre;
    private String numero;
    private int id;
    private boolean favorito;

    public Contacto(String nombre, String numero, int id) {
        this.nombre = nombre;
        this.numero = numero;
        this.id = id;
        this.favorito = false;
    }

    public Contacto(String nombre, String numero, int id, boolean favorito) {
        this.nombre = nombre;
        this.numero = numero;
        this.id = id;
        this.favorito = favorito;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNumero() {
        return numero;
    }

    public int getId() {
        return id;
    }

    public boolean esFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
