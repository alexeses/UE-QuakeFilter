package com.github.quakefilter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.quakefilter.entities.Terremoto;

import java.util.List;

public class TerremotoAdapter extends RecyclerView.Adapter<TerremotoAdapter.TerremotoVH> {

    List<Terremoto> terremotos;

    public TerremotoAdapter(List<Terremoto> datos) {
        this.terremotos = datos;
    }

    @NonNull
    @Override
    public TerremotoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_terremoto, parent, false);
        return new TerremotoVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TerremotoVH holder, int position) {
        holder.bindTerremoto(terremotos.get(position));
    }

    @Override
    public int getItemCount() {
        return terremotos.size();
    }

    public static class TerremotoVH extends RecyclerView.ViewHolder {
        TextView tvNombre, tvMagnitud, tvCoordenadas, tvLugar, tvFecha, tvMuertos;
        public TerremotoVH(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreTerremoto);
            tvMagnitud = itemView.findViewById(R.id.tvMagnitudTerremoto);
            tvCoordenadas = itemView.findViewById(R.id.tvCoordenadasTerremoto);
            tvLugar = itemView.findViewById(R.id.tvLugarTerremoto);
            tvFecha = itemView.findViewById(R.id.tvFechaTerremoto);
            tvMuertos = itemView.findViewById(R.id.tvMuertosTerremoto);
        }

        public void bindTerremoto(Terremoto terremoto) {
            tvNombre.setText(terremoto.getNombreDispositivo());
            tvMagnitud.setText("Magnitud: " + terremoto.getMagnitud());
            tvCoordenadas.setText("Ubi: " + terremoto.getCoordenadasEpicentro());
            tvLugar.setText("Lugar: " + terremoto.getLugar());
            tvFecha.setText("Fecha: " + terremoto.getFechaHora());
            tvMuertos.setText("Muertos: " + terremoto.getCantidadMuertos());
        }
    }
}