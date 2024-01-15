package com.example.maestromultiplicacion20.modelo;

/**
 * Clase para guardas los contactos
 */
public class Contacto {
    private String nombre;
    private String email;
    private int id;
    private boolean favorito;

    public Contacto(String nombre, String email, int id) {
        this.nombre = nombre;
        this.email = email;
        this.id = id;
        this.favorito = false;
    }

    public Contacto(String nombre, String email, int id, boolean favorito) {
        this.nombre = nombre;
        this.email = email;
        this.id = id;
        this.favorito = favorito;
    }


    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
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
