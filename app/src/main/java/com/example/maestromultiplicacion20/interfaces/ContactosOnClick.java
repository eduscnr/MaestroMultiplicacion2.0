package com.example.maestromultiplicacion20.interfaces;

/**
 * Interfaz que se va a implementar en el adaptadorContactos
 */
public interface ContactosOnClick {
    /**
     * Método que le pasan por paramentro la posicion del RecyclerView y reailzar acciones de favoritos y eleminar favoritos
     * @param posicion posicion del RecyclerView clicado
     */
    public void onClickContactos(int posicion);
    public void onClickCardView(int posicion);
}
