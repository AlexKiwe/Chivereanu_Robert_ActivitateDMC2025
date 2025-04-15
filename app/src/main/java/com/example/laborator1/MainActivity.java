package com.example.laborator1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView listView;
    private ArrayAdapter<Calculator> adapter;
    private List<Calculator> listaMagazine;
    private Switch switchAdapter;
    private CustomAdapter customAdapter;

    private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editTextUserInput);
        Button button = findViewById(R.id.buttonChangeText);
        Button buttonGoToSecondActivity = findViewById(R.id.buttonGoToSecondActivity);
        Button buttonAdauga = findViewById(R.id.buttonAdaugaMagazin);
        Button buttonSetari = findViewById(R.id.buttonSetari);
        listView = findViewById(R.id.listViewMagazin);
        switchAdapter = findViewById(R.id.switchAdapter);

        listaMagazine = new ArrayList<>();
        incarcaCalculatoare();

        adapter = new ArrayAdapter<Calculator>(this, android.R.layout.simple_list_item_1, listaMagazine) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                int dim = prefs.getInt("dimensiune_text", 16);
                int culoare = prefs.getInt("culoare_text", Color.BLACK);

                text.setTextSize(dim);
                text.setTextColor(culoare);
                return view;
            }
        };

        customAdapter = new CustomAdapter(this, listaMagazine);
        listView.setAdapter(adapter);

        switchAdapter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                listView.setAdapter(customAdapter);
            } else {
                listView.setAdapter(adapter);
            }
        });

        Button buttonDatabase = findViewById(R.id.buttonDatabase);
        buttonDatabase.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RoomActivity.class);
            startActivity(intent);
        });


        button.setOnClickListener(v -> {
            String newText = editText.getText().toString();
            textView.setText(newText.isEmpty() ? getString(R.string.new_text) : newText);
        });

        buttonGoToSecondActivity.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        });

        buttonSetari.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SetariActivity.class));
        });

        buttonAdauga.setOnClickListener(v -> {
            startActivityForResult(new Intent(MainActivity.this, NewActivity.class), 1);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Calculator selected = listaMagazine.get(position);
            Intent intent = new Intent(MainActivity.this, NewActivity.class);
            intent.putExtra("selectedItem", (Parcelable) selected);
            intent.putExtra("position", position);
            startActivityForResult(intent, 2);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Calculator favorit = listaMagazine.get(position);
            salveazaInFavorite(favorit);
            Toast.makeText(MainActivity.this, "Adaugat la favorite!", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void incarcaCalculatoare() {
        try {
            FileInputStream fis = openFileInput("calculatoare.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (true) {
                Calculator c = (Calculator) ois.readObject();
                listaMagazine.add(c);
            }
        } catch (EOFException e) {
            Log.d(TAG, "Toate obiectele au fost incarcate.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void salveazaInFavorite(Calculator calculator) {
        try {
            FileOutputStream fos = openFileOutput("favorite.dat", MODE_APPEND);
            ObjectOutputStream oos = new NewActivity.AppendableObjectOutputStream(fos);
            oos.writeObject(calculator);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void aplicaPreferinteText() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int dim = prefs.getInt("dimensiune_text", 16);
        int culoare = prefs.getInt("culoare_text", Color.BLACK);
        applyStyleToAllTextViews((ViewGroup) findViewById(android.R.id.content), dim, culoare);
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

    @Override
    protected void onResume() {
        super.onResume();
        aplicaPreferinteText();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Calculator magazin = data.getParcelableExtra("magazin");
            int pozitie = data.getIntExtra("position", -1);

            if (requestCode == 1) {
                listaMagazine.add(magazin);
                salveazaInFisier(magazin);
            } else if (requestCode == 2 && pozitie != -1) {
                listaMagazine.set(pozitie, magazin);
            }

            adapter.notifyDataSetChanged();
            customAdapter.notifyDataSetChanged();
        }
    }

    private void salveazaInFisier(Calculator calculator) {
        try {
            FileOutputStream fos = openFileOutput("calculatoare.dat", MODE_APPEND);
            ObjectOutputStream oos = new NewActivity.AppendableObjectOutputStream(fos);
            oos.writeObject(calculator);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
