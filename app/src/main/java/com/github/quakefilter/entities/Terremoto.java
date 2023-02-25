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
    private int cantidadMuertos;

    // Constructor
    public Terremoto(String fechaHora, String nombreDispositivo, double magnitud,
                     String coordenadasEpicentro, String lugar, int cantidadMuertos) {
        this.fechaHora = fechaHora;
        this.nombreDispositivo = nombreDispositivo;
        this.magnitud = magnitud;
        this.coordenadasEpicentro = coordenadasEpicentro;
        this.lugar = lugar;
        this.cantidadMuertos = cantidadMuertos;
    }

    // Getters y setters
    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
    public String getNombreDispositivo() { return nombreDispositivo; }
    public void setNombreDispositivo(String nombreDispositivo) { this.nombreDispositivo = nombreDispositivo; }
    public double getMagnitud() { return magnitud; }
    public void setMagnitud(double magnitud) { this.magnitud = magnitud; }
    public String getCoordenadasEpicentro() { return coordenadasEpicentro; }
    public void setCoordenadasEpicentro(String coordenadasEpicentro) { this.coordenadasEpicentro = coordenadasEpicentro; }
    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }
    public int getCantidadMuertos() { return cantidadMuertos; }
    public void setCantidadMuertos(int cantidadMuertos) { this.cantidadMuertos = cantidadMuertos; }
}
