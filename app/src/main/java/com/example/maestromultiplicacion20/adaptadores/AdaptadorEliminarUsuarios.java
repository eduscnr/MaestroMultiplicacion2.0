package com.example.maestromultiplicacion20.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.modelo.UsuarioPersonalizado;

import java.util.List;

public class AdaptadorEliminarUsuarios extends RecyclerView.Adapter<AdaptadorEliminarUsuarios.ViewHolder>{
    private List<UsuarioPersonalizado> itemList;
    private Context context;

    public AdaptadorEliminarUsuarios(List<UsuarioPersonalizado> itemList, Context context) {
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageButton imageButton;
        private TextView textView;
        private Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.imageButton);
            textView = itemView.findViewById(R.id.textView);
            this.context = context;
            imageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            System.out.println(textView.getText().toString());
        }
    }
}
