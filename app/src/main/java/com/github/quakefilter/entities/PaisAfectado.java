package com.github.quakefilter.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "PAISES_AFECTADOS",
        primaryKeys = {"fechaHora", "pais"},
        foreignKeys = @ForeignKey(entity = Terremoto.class,
                parentColumns = "fechaHora",
                childColumns = "fechaHora"))
public class PaisAfectado {
    @NotNull
    @ColumnInfo(name = "fechaHora")
    private String fechaHora;

    @NotNull
    @ColumnInfo(name = "pais")
    private String pais;

    // Constructor
    public PaisAfectado(String fechaHora, String pais) {
        this.fechaHora = fechaHora;
        this.pais = pais;
    }

    // Getters y setters
    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    @Override
    public String toString() {
        return "PaisAfectado{" +
                "fechaHora='" + fechaHora + '\'' +
                ", pais='" + pais + '\'' +
                '}';
    }
}
