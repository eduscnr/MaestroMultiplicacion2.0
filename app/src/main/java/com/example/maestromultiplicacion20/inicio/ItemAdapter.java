package com.example.maestromultiplicacion20.inicio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maestromultiplicacion20.MainActivity;
import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.modelo.Usuario;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> itemList;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public interface OnItemClickListener {
        void onItemButtonClick();
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public ItemAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.imageButton.setImageResource(item.getImageResource());
        holder.textView.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton imageButton;
        TextView textView;
        Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.imageButton);
            textView = itemView.findViewById(R.id.textView);
            imageButton.setOnClickListener(this);
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            if (textView.getText().toString().equalsIgnoreCase("Crear")) {
                onItemClickListener.onItemButtonClick();
            }else{
                for (Usuario u:MainActivityPrincipal.getUsuarios()){
                    if(u.getNombreUsuario().equalsIgnoreCase(textView.getText().toString()) && u.getTipoCuenta().equalsIgnoreCase("usuario")){
                        MainActivityPrincipal.setUsuarioLogeado(u);
                        Intent i = new Intent(context, MainActivity.class);
                        context.startActivity(i);
                    }else if(u.getNombreUsuario().equalsIgnoreCase(textView.getText().toString()) && u.getTipoCuenta().equalsIgnoreCase("administrador")){
                        MainActivityPrincipal.setUsuarioLogeado(u);
                        Intent i = new Intent(context, MainActivitySingIn.class);
                        context.startActivity(i);
                    }
                }
            }
        }
    }
}
