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
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        public void onClickItem(int posicion);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

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
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClickItem(holder.getAdapterPosition());
            }
        });
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageButton imageButton;
        private TextView textView;
        private Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.imageButton);
            textView = itemView.findViewById(R.id.textView);
            this.context = context;
        }
    }
}
