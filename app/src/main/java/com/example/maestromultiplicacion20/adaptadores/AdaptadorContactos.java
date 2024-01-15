package com.example.maestromultiplicacion20.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maestromultiplicacion20.R;
import com.example.maestromultiplicacion20.interfaces.ContactosOnClick;
import com.example.maestromultiplicacion20.modelo.Contacto;

import java.util.List;

/**
 * Adaptador para mostrar los contactos y los contactor favoritos
 */
public class AdaptadorContactos extends RecyclerView.Adapter<AdaptadorContactos.ContactosViewHolder> {
    private List<Contacto> contactos;
    private ContactosOnClick contactosOnClick;
    private Context context;

    public AdaptadorContactos(List<Contacto> contactos, ContactosOnClick contactosOnClick) {
        this.contactos = contactos;
        this.contactosOnClick = contactosOnClick;
    }

    @Override
    public AdaptadorContactos.ContactosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contactos, parent, false);
        return new ContactosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactosViewHolder holder, int position) {
        Contacto c = contactos.get(position);
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.nombre.setText(c.getNombre());
        holder.numero.setText(c.getEmail());
        if(c.esFavorito()){
            holder.favorito.setImageResource(R.drawable.estrella_amarilla_32);
        }else{
            holder.favorito.setImageResource(R.drawable.icons8_estrella_32);
        }
        ponerLetra(c.getNombre().charAt(0), holder);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int posicion =holder.getAdapterPosition();
                contactosOnClick.onClickCardView(posicion);
            }
        });
        holder.favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int posicion =holder.getAdapterPosition();
                if(contactosOnClick != null){
                    contactosOnClick.onClickContactos(posicion);
                    holder.favorito.setImageResource(R.drawable.estrella_amarilla_32);
                }
            }
        });
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }
    private void ponerLetra(char charAt, AdaptadorContactos.ContactosViewHolder holder) {
        switch (Character.toLowerCase(charAt)){
            case 'a':
                holder.letra.setImageResource(R.drawable.icons8_a_48);
                break;
            case 'b':
                holder.letra.setImageResource(R.drawable.icons8_b_48);
                break;
            case 'c':
                holder.letra.setImageResource(R.drawable.icons8_c_48);
                break;
            case 'd':
                holder.letra.setImageResource(R.drawable.icons8_d_48);
                break;
            case 'e':
                holder.letra.setImageResource(R.drawable.icons8_e_48);
                break;
            case 'f':
                holder.letra.setImageResource(R.drawable.icons8_f_48);
                break;
            case 'g':
                holder.letra.setImageResource(R.drawable.icons8_g_48);
                break;
            case 'h':
                holder.letra.setImageResource(R.drawable.icons8_h_48);
                break;
            case 'i':
                holder.letra.setImageResource(R.drawable.icons8_i_48);
                break;
            case 'j':
                holder.letra.setImageResource(R.drawable.icons8_j_48);
                break;
            case 'l':
                holder.letra.setImageResource(R.drawable.icons8_l_48);
                break;
            case 'm':
                holder.letra.setImageResource(R.drawable.icons8_m_48);
                break;
            case 'n':
                holder.letra.setImageResource(R.drawable.icons8_n_48);
                break;
            case 'o':
                holder.letra.setImageResource(R.drawable.icons8_o_48);
                break;
            case 'p':
                holder.letra.setImageResource(R.drawable.icons8_p_48);
                break;
            case 'q':
                holder.letra.setImageResource(R.drawable.icons8_q_48);
                break;
            case 'r':
                holder.letra.setImageResource(R.drawable.icons8_r_48);
                break;
            case 's':
                holder.letra.setImageResource(R.drawable.icons8_s_48);
                break;
            case 't':
                holder.letra.setImageResource(R.drawable.icons8_t_48);
                break;
            case 'u':
                holder.letra.setImageResource(R.drawable.icons8_u_48);
                break;
            case 'v':
                holder.letra.setImageResource(R.drawable.icons8_v_48);
                break;
            case 'w':
                holder.letra.setImageResource(R.drawable.icons8_w_48);
                break;
            case 'y':
                holder.letra.setImageResource(R.drawable.icons8_y_48);
                break;
            case 'x':
                holder.letra.setImageResource(R.drawable.icons8_x_48);
                break;
            case 'z':
                holder.letra.setImageResource(R.drawable.icons8_z_48);
                break;
            default:
                holder.letra.setImageResource(R.drawable.icons8_usuario_48);
        }
    }

    public static class ContactosViewHolder extends RecyclerView.ViewHolder {
        private TextView nombre;
        private TextView numero;
        private ImageView letra;
        private ImageView favorito;
        private CardView cardView;
        public ContactosViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombreContacto);
            numero = itemView.findViewById(R.id.tvNumeroContacto);
            letra = itemView.findViewById(R.id.imgLetra);
            favorito = itemView.findViewById(R.id.ibFavorito);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
