package com.github.quakefilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.github.quakefilter.dao.PaisesAfectadosDAO;
import com.github.quakefilter.dao.TerremotoDAO;
import com.github.quakefilter.data.PaisesAfectados;
import com.github.quakefilter.data.Terremotos;
import com.github.quakefilter.database.TerremotosDB;
import com.github.quakefilter.entities.PaisAfectado;
import com.github.quakefilter.entities.Terremoto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements DialogFilter.OnFilterSelectedListener {

    private TerremotosDB TerremotosDB;
    private TerremotoAdapter adapter;
    private List<Terremoto> datosTerremotos;
    private RecyclerView vTerremotos;

    private Button btnDialog;
    private Button btnShowData;
    private TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDialog = findViewById(R.id.btn_dialog);
        btnShowData = findViewById(R.id.btn_show_data);
        tvData = findViewById(R.id.tv_data);
        vTerremotos = findViewById(R.id.v_terremotos);

        btnDialog.setOnClickListener(v -> {
            DialogFilter dialogFilter = new DialogFilter();
            dialogFilter.show(getSupportFragmentManager(), "DialogFilter");
        });

        btnShowData.setOnClickListener(v -> {
            datosTerremotos = new ArrayList<>();
            adapter = new TerremotoAdapter(datosTerremotos);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            vTerremotos.setLayoutManager(layoutManager);
            vTerremotos.setItemAnimator(new DefaultItemAnimator());
            vTerremotos.setAdapter(adapter);


            TerremotosDB = TerremotosDB.getDatabase(this);
            TerremotoDAO tDao = TerremotosDB.terremotoDAO();

            List<Terremoto> terremotosList = tDao.obtenerTodosLosTerremotos();
            TerremotoAdapter adapter = new TerremotoAdapter(terremotosList);
            vTerremotos.setAdapter(adapter);

        });

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
            System.out.println("Total de paises afectados: " + pAfect.obtenerTodosLosPaisesAfectados().size());

            //tDao.obtenerTodosLosTerremotos().forEach(terremoto -> tDao.borrarTerremoto(terremoto));
            // Borrar todos los paises afectados
            //tDao.borrarTodosLosTerremotos();
            //pAfect.borrarTodosLosPaisesAfectados();

            if (tDao.obtenerTodosLosTerremotos().isEmpty()) {
                System.out.println("Base de datos creada correctamente");

                List<Terremoto> terremotos = Terremotos.getTerremotosIniciales();

                for (Terremoto terremoto : terremotos) {
                    tDao.insertarTerremoto(terremoto);
                    System.out.println("Terremoto insertado: " + terremoto.toString());
                }
            }

            if (pAfect.obtenerTodosLosPaisesAfectados().isEmpty()) {
                System.out.println("Base de datos creada correctamente");

                List<PaisAfectado> paisesAfectados = PaisesAfectados.getPaisesAfectadosIniciales();

                for (PaisAfectado paisAfectado : paisesAfectados) {
                    pAfect.insertarPaisAfectado(paisAfectado);
                    System.out.println("PaisAfectado insertado: " + paisAfectado.toString());
                }
            }

        });
    }

    @Override
    public void onFilterSelected(String filter) {
        tvData.setText(filter);
    }
}