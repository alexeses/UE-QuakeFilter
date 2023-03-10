package com.github.quakefilter.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.github.quakefilter.entities.Terremoto;

import java.util.List;

@Dao
public interface TerremotoDAO {

    @Insert
    void insertarTerremoto(Terremoto terremoto);

    @Query("SELECT * FROM TERREMOTOS" + " ORDER BY magnitud DESC")
    List<Terremoto> obtenerTodosLosTerremotos();

    // Borra todos los terremotos
    @Query("DELETE FROM TERREMOTOS")
    void borrarTodosLosTerremotos();

    // Terremotos ordenados por magnitud
    @Query("SELECT * FROM TERREMOTOS ORDER BY magnitud DESC")
    List<Terremoto> ordenarPorMagnitud();

    // Obten todos los terremotos por pais
    @Query("SELECT TERREMOTOS.* FROM TERREMOTOS, PAISES_AFECTADOS WHERE TERREMOTOS.fechaHora = " +
            "PAISES_AFECTADOS.fechaHora AND PAISES_AFECTADOS.pais LIKE :pais" + " ORDER BY magnitud DESC")
    List<Terremoto> obtenerTerremotosPorPais(String pais);

    @Query("SELECT TERREMOTOS.* FROM TERREMOTOS, PAISES_AFECTADOS WHERE TERREMOTOS.fechaHora = " +
            "PAISES_AFECTADOS.fechaHora AND PAISES_AFECTADOS.pais LIKE :pais" + " ORDER BY magnitud DESC")
    List<Terremoto> obtenerTerremotosPorPaisSinOrdenar(String pais);

    // Case: obtener terremotos por operador y magnitud
    @Query("SELECT * FROM TERREMOTOS WHERE " +
            "CASE " +
            "WHEN :operador = '>' AND magnitud > :valorMagnitud THEN 1 " +
            "WHEN :operador = '<' AND magnitud < :valorMagnitud THEN 1 " +
            "WHEN :operador = '==' AND magnitud = :valorMagnitud THEN 1 " +
            "WHEN :operador = '>=' AND magnitud >= :valorMagnitud THEN 1 " +
            "WHEN :operador = '<=' AND magnitud <= :valorMagnitud THEN 1 " +
            "WHEN :operador = '!=' AND magnitud != :valorMagnitud THEN 1 " +
            "ELSE 0 " +
            "END = 1" +
            " ORDER BY magnitud DESC")
    List<Terremoto> obtenerTerremotosPorOperadorYMagnitud(String operador, String valorMagnitud);

    // Case: obtener terremotos por pais, operador y magnitud
    @Query("SELECT TERREMOTOS.* FROM TERREMOTOS, PAISES_AFECTADOS WHERE TERREMOTOS.fechaHora = " +
            "PAISES_AFECTADOS.fechaHora AND PAISES_AFECTADOS.pais LIKE :country AND " +
            "CASE " +
            "WHEN :operator = '>' AND magnitud > :magnitude THEN 1 " +
            "WHEN :operator = '<' AND magnitud < :magnitude THEN 1 " +
            "WHEN :operator = '==' AND magnitud = :magnitude THEN 1 " +
            "WHEN :operator = '>=' AND magnitud >= :magnitude THEN 1 " +
            "WHEN :operator = '<=' AND magnitud <= :magnitude THEN 1 " +
            "WHEN :operator = '!=' AND magnitud != :magnitude THEN 1 " +
            "ELSE 0 " +
            "END = 1" +
            " ORDER BY magnitud DESC")
    List<Terremoto> obtenerTerremotosPorPaisOperadorYMagnitud(String country, String operator, String magnitude);
}
