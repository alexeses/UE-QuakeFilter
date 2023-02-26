package com.github.quakefilter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.github.quakefilter.database.TerremotosDB;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DialogFilter extends DialogFragment {
    private EditText magnitudeEditText;
    private Spinner operatorSpinner;
    private Spinner countrySpinner;
    private OnFilterSelectedListener listener;

    public interface OnFilterSelectedListener {
        void onFilterSelected(String filterText, String country, String operator, String magnitude);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnFilterSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OnFilterSelectedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter, null);

        magnitudeEditText = view.findViewById(R.id.edittext_magnitude);
        operatorSpinner = view.findViewById(R.id.spinner_operator);
        countrySpinner = view.findViewById(R.id.spinner_country);

        ArrayAdapter<CharSequence> operatorAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.operators, android.R.layout.simple_spinner_item);
        operatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operatorSpinner.setAdapter(operatorAdapter);

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);

        List<String> countries = new ArrayList<>();
        // Se utiliza un executor para ejecutar la consulta a la base de datos en segundo plano
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Se hace una consulta a la base de datos para obtener la lista de países
                TerremotosDB db = TerremotosDB.getDatabase(requireContext());
                countries.add("Global");
                countries.addAll(db.paisesAfectadosDAO().obtenerNombresPaisesAfectados());
                // Se actualiza la interfaz de usuario con los resultados de la consulta
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!countries.isEmpty()) {
                            countryAdapter.addAll(countries);
                        }
                    }
                });
            }
        });

        builder.setView(view)
                .setTitle("Filtrar Terremotos")
                .setPositiveButton("Filtrar", (dialog, id) -> {
                    String operator = operatorSpinner.getSelectedItem().toString();
                    String magnitudeString = magnitudeEditText.getText().toString();
                    String country = countrySpinner.getSelectedItem().toString();

                    double magnitude;
                    try {
                        magnitude = Double.parseDouble(magnitudeString);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Introduce una magnitud válida", Toast.LENGTH_SHORT).show();
                        magnitudeEditText.requestFocus();
                        return;
                    }

                    if (magnitude < 0 || magnitude > 10) {
                        Toast.makeText(getContext(), "La magnitud debe estar entre 0 y 10", Toast.LENGTH_SHORT).show();
                        magnitudeEditText.requestFocus();
                        return;
                    }

                    String filterText = "";
                    if (!magnitudeString.isEmpty()) {
                        if (operator.isEmpty()) {
                            // Si se introdujo la magnitud pero no el operador, se pide el operador
                            Toast.makeText(getContext(), "Introduce el operador correspondiente", Toast.LENGTH_SHORT).show();
                            operatorSpinner.performClick();
                            return;
                        } else {
                            filterText = "Magnitud: " + operator + " " + magnitudeString + ", ";
                        }
                    } else if (!operator.isEmpty()) {
                        // Si se introdujo el operador pero no la magnitud, se pide la magnitud
                        Toast.makeText(getContext(), "Introduce la magnitud correspondiente", Toast.LENGTH_SHORT).show();
                        magnitudeEditText.requestFocus();
                        return;
                    }

                    if (!country.isEmpty()) {
                        filterText += "País: " + country;
                    }

                    listener.onFilterSelected(filterText, country, operator, magnitudeString);
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    // Se cierra el diálogo sin enviar ningún filtro
                    dismiss();
                });
        return builder.create();

    }
}