package com.github.quakefilter.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "TERREMOTOS",
        indices = {@Index(value = {"nombreDispositivo"}, unique = true)})
public class Terremoto {
    @NotNull
    @PrimaryKey
    @ColumnInfo(name = "fechaHora")
    private String fechaHora;

    @NotNull
    @ColumnInfo(name = "nombreDispositivo")
    private String nombreDispositivo;

    @ColumnInfo(name = "magnitud")
    private double magnitud;

    @ColumnInfo(name = "coordenadasEpicentro")
    private String coordenadasEpicentro;

    @ColumnInfo(name = "lugar")
    private String lugar;

    @ColumnInfo(name = "cantidadMuertos")
    private String cantidadMuertos;

    // Constructor
    public Terremoto(String fechaHora, double magnitud, String nombreDispositivo,
                     String coordenadasEpicentro, String lugar, String cantidadMuertos) {
        this.fechaHora = fechaHora;
        this.nombreDispositivo = nombreDispositivo;
        this.magnitud = magnitud;
        this.coordenadasEpicentro = coordenadasEpicentro;
        this.lugar = lugar;
        this.cantidadMuertos = cantidadMuertos;
    }

    // Getters
    public String getFechaHora() { return fechaHora; }
    public String getNombreDispositivo() { return nombreDispositivo; }
    public double getMagnitud() { return magnitud; }
    public String getCoordenadasEpicentro() { return coordenadasEpicentro; }
    public String getLugar() { return lugar; }
    public String getCantidadMuertos() { return cantidadMuertos; }

    // ToString method
    @Override
    public String toString() {
        return "Terremoto{" +
                "fechaHora='" + fechaHora + '\'' +
                ", nombreDispositivo='" + nombreDispositivo + '\'' +
                ", magnitud=" + magnitud +
                ", coordenadasEpicentro='" + coordenadasEpicentro + '\'' +
                ", lugar='" + lugar + '\'' +
                ", cantidadMuertos='" + cantidadMuertos + '\'' +
                '}';
    }
}
