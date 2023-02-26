package com.github.quakefilter.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.github.quakefilter.dao.PaisesAfectadosDAO;
import com.github.quakefilter.dao.TerremotoDAO;
import com.github.quakefilter.entities.PaisAfectado;
import com.github.quakefilter.entities.Terremoto;

@Database(entities = {Terremoto.class, PaisAfectado.class}, version = 1, exportSchema = false)
public abstract class TerremotosDB extends RoomDatabase {
    private static TerremotosDB INSTANCE;
    public abstract TerremotoDAO terremotoDAO();
    public abstract PaisesAfectadosDAO paisesAfectadosDAO();

    public static TerremotosDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TerremotosDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TerremotosDB.class, "TERREMOTOS_DB")
                            .allowMainThreadQueries() // TODO: Retirar
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
