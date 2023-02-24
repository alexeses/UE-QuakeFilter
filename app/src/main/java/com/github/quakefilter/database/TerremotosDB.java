package com.github.quakefilter.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.github.quakefilter.dao.PaisesAfectados;
import com.github.quakefilter.dao.Terremotos;

@Database(entities = {Terremotos.class, PaisesAfectados.class}, version = 1, exportSchema = false)
public abstract class TerremotosDB extends RoomDatabase {
    private static TerremotosDB INSTANCE;

    public static TerremotosDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TerremotosDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TerremotosDB.class, "terremotos_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
