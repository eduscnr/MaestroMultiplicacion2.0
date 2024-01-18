package com.example.maestromultiplicacion20.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maestromultiplicacion20.inicio.ActividadNavegationDrawer;
import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.inicio.ActividadPrincipal;
import com.example.maestromultiplicacion20.inicio.ActividadSignIn;
import com.example.maestromultiplicacion20.modelo.UsuarioPersonalizado;
import com.example.maestromultiplicacion20.modelo.Usuario;

import java.util.List;

/**
 * Clase adaptador para el UsuarioPersonalizado
 */
public class UsuarioPersonalizadoAdapter extends RecyclerView.Adapter<UsuarioPersonalizadoAdapter.ViewHolder> {
    private List<UsuarioPersonalizado> itemList;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public void setItemList(List<UsuarioPersonalizado> itemList) {
        this.itemList = itemList;
    }

    public interface OnItemClickListener {
        void onItemButtonClick();
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public UsuarioPersonalizadoAdapter(List<UsuarioPersonalizado> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.usuario_personalizados_layout, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsuarioPersonalizado item = itemList.get(position);
        holder.imageButton.setImageResource(item.getImageResource());
        holder.textView.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageButton imageButton;
        private TextView textView;
        private Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.imageButton);
            textView = itemView.findViewById(R.id.textView);
            imageButton.setOnClickListener(this);
            this.context = context;
        }

        /**
         * Método dependiendo del usuario que quierea iniciar te pedira una clave o no
          * @param view
         */
        @Override
        public void onClick(View view) {
            if (textView.getText().toString().equalsIgnoreCase("Crear")) {
                onItemClickListener.onItemButtonClick();
            }else{
                for (Usuario u: ActividadPrincipal.getUsuarios()){
                    if(u.getNombreUsuario().equalsIgnoreCase(textView.getText().toString()) && u.getTipoCuenta().equalsIgnoreCase("usuario")){
                        ActividadPrincipal.setUsuarioLogeado(u);
                        Intent i = new Intent(context, ActividadNavegationDrawer.class);
                        context.startActivity(i);
                    }else if(u.getNombreUsuario().equalsIgnoreCase(textView.getText().toString()) && u.getTipoCuenta().equalsIgnoreCase("administrador")){
                        ActividadPrincipal.setUsuarioLogeado(u);
                        Intent i = new Intent(context, ActividadSignIn.class);
                        context.startActivity(i);
                    }
                }
            }
        }
    }
}
