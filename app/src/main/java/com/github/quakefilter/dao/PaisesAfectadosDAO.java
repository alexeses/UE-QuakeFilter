package com.github.quakefilter.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.github.quakefilter.entities.PaisAfectado;

import java.util.List;

@Dao
public interface PaisesAfectadosDAO {

    @Insert
    void insertarPaisAfectado(PaisAfectado paisAfectado);

    @Query("SELECT * FROM PAISES_AFECTADOS")
    List<PaisAfectado> obtenerTodosLosPaisesAfectados();

    @Query("DELETE FROM PAISES_AFECTADOS")
    void borrarTodosLosPaisesAfectados();

    @Query("SELECT DISTINCT pais FROM PAISES_AFECTADOS")
    List<String> obtenerNombresPaisesAfectados();
}