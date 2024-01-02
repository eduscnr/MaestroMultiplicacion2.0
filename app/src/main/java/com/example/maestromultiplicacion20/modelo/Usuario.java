package com.example.maestromultiplicacion20.modelo;

/**
 * Clase para guardar los usuarios
 */
public class Usuario {
    private String nombreUsuario;
    private String tipoCuenta;
    private int avatarImg;
    private String contrasenia;

    public Usuario(String nombreUsuario, String tipoCuenta, int avatarImg, String contrasenia) {
        this.nombreUsuario = nombreUsuario;
        this.tipoCuenta = tipoCuenta;
        this.avatarImg = avatarImg;
        this.contrasenia = contrasenia;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public int getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(int avatarImg) {
        this.avatarImg = avatarImg;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombreUsuario='" + nombreUsuario + '\'' +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                ", avatarImg=" + avatarImg +
                ", contrasenia='" + contrasenia + '\'' +
                '}';
    }
}
