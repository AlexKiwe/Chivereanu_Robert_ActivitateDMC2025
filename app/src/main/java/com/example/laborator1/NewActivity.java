// =========================
// MainActivity.java
// =========================

// (codul deja prezent)


// =========================
// NewActivity.java
// =========================

package com.example.laborator1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

public class NewActivity extends AppCompatActivity {

    private EditText editTextNume;
    private CheckBox checkBoxDisponibilitate;
    private Spinner spinnerCategorie;
    private RatingBar ratingBar;
    private RadioButton radioButtonElectronics, radioButtonClothing;
    private Switch switchStatus;
    private ToggleButton toggleButton;
    private Button dateButton;
    private TextView textViewData;

    private Date selectedDate;
    private int pozitieEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        editTextNume = findViewById(R.id.editTextNume);
        checkBoxDisponibilitate = findViewById(R.id.checkBoxDisponibilitate);
        spinnerCategorie = findViewById(R.id.spinnerCategorie);
        ratingBar = findViewById(R.id.ratingBar);
        radioButtonElectronics = findViewById(R.id.radioButtonJewelry);
        radioButtonClothing = findViewById(R.id.radioButtonClothing);
        switchStatus = findViewById(R.id.switchStatus);
        toggleButton = findViewById(R.id.toggleButton);
        dateButton = findViewById(R.id.buttonSelectDate);
        textViewData = findViewById(R.id.textViewData);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categorii_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(adapter);

        selectedDate = new Date();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int dim = prefs.getInt("dimensiune_text", 16);
        int culoare = prefs.getInt("culoare_text", Color.BLACK);

        editTextNume.setTextSize(dim);
        editTextNume.setTextColor(culoare);
        textViewData.setTextSize(dim);
        textViewData.setTextColor(culoare);

        dateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(selectedYear, selectedMonth, selectedDay);
                        selectedDate = selectedCalendar.getTime();
                        textViewData.setText("Data selectată: " + selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                    }, year, month, day);
            datePickerDialog.show();
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedItem")) {
            Calculator received = intent.getParcelableExtra("selectedItem");
            pozitieEdit = intent.getIntExtra("position", -1);

            if (received != null) {
                editTextNume.setText(received.getNume());
                checkBoxDisponibilitate.setChecked(received.isDisponibilitate());
                ratingBar.setRating(received.getRating());

                String[] categoriiArray = getResources().getStringArray(R.array.categorii_array);
                for (int i = 0; i < categoriiArray.length; i++) {
                    if (categoriiArray[i].equalsIgnoreCase(received.getCategorie().toString())) {
                        spinnerCategorie.setSelection(i);
                        break;
                    }
                }

                if (received.getCategorie() == Calculator.Category.PC) {
                    radioButtonElectronics.setChecked(true);
                } else {
                    radioButtonClothing.setChecked(true);
                }

                switchStatus.setChecked(received.getStatus());
                toggleButton.setChecked(received.isFunctional());

                selectedDate = received.getDateTime();
                Calendar cal = Calendar.getInstance();
                cal.setTime(selectedDate);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH) + 1;
                int year = cal.get(Calendar.YEAR);
                textViewData.setText("Data selectată: " + day + "/" + month + "/" + year);
            }
        }

        Button buttonReturn = findViewById(R.id.buttonReturn);
        buttonReturn.setOnClickListener(v -> {
            String nume = editTextNume.getText().toString();

            if (nume.isEmpty()) {
                editTextNume.setError("Numele este obligatoriu!");
                return;
            }

            boolean disponibilitate = checkBoxDisponibilitate.isChecked();
            String categorieMagazin = spinnerCategorie.getSelectedItem().toString();

            Calculator.Category categorie;
            if (categorieMagazin.equalsIgnoreCase("PC")) {
                categorie = Calculator.Category.PC;
            } else if (categorieMagazin.equalsIgnoreCase("Laptop")) {
                categorie = Calculator.Category.LAPTOP;
            } else {
                categorie = Calculator.Category.OTHER;
            }

            if (radioButtonElectronics.isChecked()) {
                categorie = Calculator.Category.PC;
            } else if (radioButtonClothing.isChecked()) {
                categorie = Calculator.Category.LAPTOP;
            }

            int rating = (int) ratingBar.getRating();
            boolean statusActiv = switchStatus.isChecked();
            boolean functional = toggleButton.isChecked();

            Calculator magazin = new Calculator(nume, disponibilitate, rating, categorie, statusActiv, functional, selectedDate);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("magazin", (Parcelable) magazin);
            resultIntent.putExtra("position", pozitieEdit);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    public static class AppendableObjectOutputStream extends ObjectOutputStream {
        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }
        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
} // end of NewActivity