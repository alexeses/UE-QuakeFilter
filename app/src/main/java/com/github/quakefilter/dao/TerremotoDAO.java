package com.github.quakefilter.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.github.quakefilter.entities.Terremoto;

import java.util.List;

@Dao
public interface TerremotoDAO {

    @Insert
    void insertarTerremoto(Terremoto terremoto);

    @Update
    void actualizarTerremoto(Terremoto terremoto);

    @Delete
    void borrarTerremoto(Terremoto terremoto);

    @Query("SELECT * FROM TERREMOTOS")
    List<Terremoto> obtenerTodosLosTerremotos();

}
