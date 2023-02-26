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
    private Spinner operatorSpinner, countrySpinner;
    private OnFilterSelectedListener listener;

    public interface OnFilterSelectedListener {
        void onFilterSelected(String filterText, String country, String operator, String magnitude);
    }

    @Override
    public void onAttach(@NonNull Context context) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
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
                .setTitle(R.string.filter)
                .setPositiveButton(R.string.btn_filter, (dialog, id) -> {
                    String operator = operatorSpinner.getSelectedItem().toString();
                    String magnitudeString = magnitudeEditText.getText().toString();
                    String country = countrySpinner.getSelectedItem().toString();

                    double magnitude;
                    try {

                        if (operator.equals("N/a") && magnitudeString.isEmpty())
                            magnitudeString = "0";
                        else {
                            if (magnitudeString.isEmpty()) {
                                Toast.makeText(getContext(), R.string.invalid_magnitude, Toast.LENGTH_SHORT).show();
                                magnitudeEditText.requestFocus();
                                return;
                            }
                        }

                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), R.string.invalid_magnitude, Toast.LENGTH_SHORT).show();
                        magnitudeEditText.requestFocus();
                        return;
                    }


                    if (operator.equals("N/a") && country.equals("Global")) {
                        Toast.makeText(getContext(), R.string.invalid_magnitude, Toast.LENGTH_SHORT).show();
                        magnitudeEditText.requestFocus();
                        return;
                    }

                    String filterText = "";
                    if (operator.isEmpty()) {
                        Toast.makeText(getContext(), R.string.wrong_operator , Toast.LENGTH_SHORT).show();
                        operatorSpinner.performClick();
                        return;
                    } else {
                        filterText = "Magnitud: " + operator + " " + magnitudeString + ", ";
                    }

                    if (!country.isEmpty()) {
                        filterText += "País: " + country;
                    }

                    listener.onFilterSelected(filterText, country, operator, magnitudeString);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    dismiss();
                });
        return builder.create();

    }
}