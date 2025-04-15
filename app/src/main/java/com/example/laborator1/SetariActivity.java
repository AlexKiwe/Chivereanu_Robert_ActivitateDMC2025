package com.example.laborator1;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetariActivity extends AppCompatActivity {
    private SeekBar seekBarDimensiune;
    private Spinner spinnerCuloare;
    private Button btnSalveaza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setari);

        seekBarDimensiune = findViewById(R.id.seekBarDimensiune);
        spinnerCuloare = findViewById(R.id.spinnerCuloare);
        btnSalveaza = findViewById(R.id.buttonSalveaza);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int dimensiuneText = prefs.getInt("dimensiune_text", 16);
        int culoareText = prefs.getInt("culoare_text", Color.BLACK);

        // Aplică stilul pe toate componentele textuale (TextView, Button etc.)
        applyStyleToAllTextViews((ViewGroup) findViewById(android.R.id.content), dimensiuneText, culoareText);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.culori_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCuloare.setAdapter(adapter);

        btnSalveaza.setOnClickListener(v -> {
            int dimensiune = seekBarDimensiune.getProgress() + 12;
            String culoareStr = spinnerCuloare.getSelectedItem().toString();

            int culoare = Color.BLACK;
            switch (culoareStr) {
                case "Roșu": culoare = Color.RED; break;
                case "Verde": culoare = Color.GREEN; break;
                case "Albastru": culoare = Color.BLUE; break;
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("dimensiune_text", dimensiune);
            editor.putInt("culoare_text", culoare);
            editor.apply();

            Toast.makeText(this, "Setări salvate!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void applyStyleToAllTextViews(ViewGroup root, int size, int color) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View view = root.getChildAt(i);
            if (view instanceof ViewGroup) {
                applyStyleToAllTextViews((ViewGroup) view, size, color);
            } else if (view instanceof TextView) {
                ((TextView) view).setTextSize(size);
                ((TextView) view).setTextColor(color);
            }
        }
    }
}
