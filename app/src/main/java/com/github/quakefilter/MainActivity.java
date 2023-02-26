package com.github.quakefilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    private Button btnDialog, btnShowData;
    private TextView tvData;
    private String country, operator, magnitude, filterText;

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

            // Usamos un Executor para ejecutar la consulta en un hilo secundario
            Executor executor = Executors.newSingleThreadExecutor();

            executor.execute(() -> {

                // Case: Sin filtro
                if (filterText != null && !filterText.isEmpty()) {

                    // Case: Terremotos de un pais
                    if (!country.isEmpty() && operator.isEmpty() && magnitude.isEmpty())
                        datosTerremotos.addAll(tDao.obtenerTerremotosPorPaisSinOrdenar(country));

                    // Case: Global, usando operador y magnitud
                    if (!operator.isEmpty() && !magnitude.isEmpty() && country.equals("Global"))
                        datosTerremotos.addAll(tDao.obtenerTerremotosPorOperadorYMagnitud(operator, magnitude));

                    // Case: Usando Pais, Operador y Magnitud
                    if (!operator.isEmpty() && !magnitude.isEmpty() && !country.isEmpty())
                        datosTerremotos.addAll(tDao.obtenerTerremotosPorPaisOperadorYMagnitud(country, operator, magnitude));

                } else
                    // Case: No hay filtro, mostrar todos los terremotos ordenados por magnitud
                    tvData.setText("Magnitud: N/a, Pais: N/a)");
                    datosTerremotos.addAll(tDao.ordenarPorMagnitud());

                // Toast no funciona en un hilo secundario, por lo que usamos runOnUiThread
                runOnUiThread(() -> {
                    if (datosTerremotos.isEmpty())
                        Toast.makeText(this, "No hay datos", Toast.LENGTH_SHORT).show();
                });

            });

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
    public void onFilterSelected(String filterText, String country, String operator, String magnitude) {
        this.filterText = filterText;
        this.country = country;
        this.operator = operator;
        this.magnitude = magnitude;

        tvData.setText(filterText);
    }
}