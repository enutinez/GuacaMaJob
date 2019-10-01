package com.enuar.prueba;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitulo, txtPrecio, txtDir;
        private CircleImageView imgPortada;

        public ViewHolder(View itemView){
            super(itemView);

            txtTitulo = (TextView)itemView.findViewById(R.id.txtTituloOf);
            txtPrecio = (TextView)itemView.findViewById(R.id.txtPrecio);
            imgPortada = (CircleImageView) itemView.findViewById(R.id.imgPortada);
            txtDir = (TextView)itemView.findViewById(R.id.txtDir);

        }

    }

    private ArrayList<Oferta> ListaOfertas;

    public RecyclerViewAdapter(ArrayList<Oferta> listaOfertas) {
        this.ListaOfertas = listaOfertas;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ofertas, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Oferta oferta = ListaOfertas.get(position);
        holder.txtTitulo.setText(oferta.getTitulo());
        holder.txtPrecio.setText(oferta.getPrecio()+"");
        holder.txtDir.setText(oferta.getDireccion());
        if (!oferta.getTipoEmergencia().equals("")){
            if (oferta.getTipoEmergencia().equals("Emergencia domestica.")){
                holder.imgPortada.setImageResource(R.drawable.house);
            }
            if (oferta.getTipoEmergencia().equals("Emergencia electrica.")){
                holder.imgPortada.setImageResource(R.drawable.electricidad);
            }
            if (oferta.getTipoEmergencia().equals("Emergencia Mecanica.")){
                holder.imgPortada.setImageResource(R.drawable.mecanica);
            }
            if (oferta.getTipoEmergencia().equals("Emergencia de transporte.")){
                holder.imgPortada.setImageResource(R.drawable.transporte);
            }
            if (oferta.getTipoEmergencia().equals("Emergencia de plomeria.")){
                holder.imgPortada.setImageResource(R.drawable.plomeria);
            }
            if (oferta.getTipoEmergencia().equals("Emergencia estilista.")){
                holder.imgPortada.setImageResource(R.drawable.estilista);
            }
            if (oferta.getTipoEmergencia().equals("Emergencia tecnologica.")){
                holder.imgPortada.setImageResource(R.drawable.tecnologia);
            }
            if (oferta.getTipoEmergencia().equals("Otro tipo de emergencia.")){
                holder.imgPortada.setImageResource(R.drawable.otros);
            }
        }


    }

    @Override
    public int getItemCount() {
        return ListaOfertas.size();
    }
}

