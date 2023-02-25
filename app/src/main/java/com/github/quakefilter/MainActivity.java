package com.github.quakefilter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.quakefilter.dao.PaisesAfectadosDAO;
import com.github.quakefilter.dao.TerremotoDAO;
import com.github.quakefilter.data.PaisesAfectados;
import com.github.quakefilter.data.Terremotos;
import com.github.quakefilter.database.TerremotosDB;
import com.github.quakefilter.entities.PaisAfectado;
import com.github.quakefilter.entities.Terremoto;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TerremotosDB TerremotosDB;
    private TerremotoDAO TerremotoDAO;

    public Terremotos d = new Terremotos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDB();

    }

    private void createDB() {
        System.out.println("Creando base de datos...");
        TerremotosDB db = TerremotosDB.getDatabase(this);

        TerremotoDAO tDao = db.terremotoDAO();
        PaisesAfectadosDAO pAfect = db.paisesAfectadosDAO();

        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {

            System.out.println("Check" + tDao.obtenerTodosLosTerremotos().isEmpty());

            System.out.println("Total de terremotos: " + tDao.obtenerTodosLosTerremotos().size());

            tDao.obtenerTodosLosTerremotos().forEach(terremoto -> tDao.borrarTerremoto(terremoto));


            if (tDao.obtenerTodosLosTerremotos().isEmpty()) {
                System.out.println("Base de datos creada correctamente");

                List<Terremoto> terremotos = Terremotos.getTerremotosIniciales();

                for (Terremoto terremoto : terremotos) {
                    tDao.insertarTerremoto(terremoto);
                    System.out.println("Terremoto insertado: " + terremoto.toString());
                }
            }
        });
    }

}