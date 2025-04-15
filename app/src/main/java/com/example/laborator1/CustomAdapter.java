
package com.example.laborator1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class CustomAdapter extends ArrayAdapter<Calculator> {
    private final Context context;
    private final List<Calculator> lista;
    private final Random random = new Random();
    private final int[] backgroundColors = {
            Color.parseColor("#FFCDD2"), Color.parseColor("#F8BBD0"),
            Color.parseColor("#D1C4E9"), Color.parseColor("#BBDEFB"),
            Color.parseColor("#C8E6C9"), Color.parseColor("#FFF9C4"),
            Color.parseColor("#FFE0B2"), Color.parseColor("#DCEDC8")
    };

    public CustomAdapter(Context context, List<Calculator> objects) {
        super(context, 0, objects);
        this.context = context;
        this.lista = objects;
    }

    private int darkenColor(int color, float factor) {
        int r = (int) ((Color.red(color)) * factor);
        int g = (int) ((Color.green(color)) * factor);
        int b = (int) ((Color.blue(color)) * factor);
        return Color.rgb(Math.max(r, 0), Math.max(g, 0), Math.max(b, 0));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Calculator calculator = lista.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_calculator, parent, false);
        }

        convertView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().translationZ(8f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().translationZ(0f).setDuration(100).start();
                    break;
            }
            return false;
        });

        TextView tvNume = convertView.findViewById(R.id.textViewNume);
        TextView tvCategorie = convertView.findViewById(R.id.textViewCategorie);
        TextView tvDisponibilitate = convertView.findViewById(R.id.textViewDisponibilitate);
        RatingBar ratingBar = convertView.findViewById(R.id.ratingBar);
        TextView tvData = convertView.findViewById(R.id.textViewData);
        TextView iconStatus = convertView.findViewById(R.id.iconStatus);
        TextView iconFunctional = convertView.findViewById(R.id.iconFunctional);
        View cardRoot = convertView.findViewById(R.id.cardRoot);

        tvNume.setText("Nume: " + calculator.getNume());
        tvCategorie.setText("Categorie: " + calculator.getCategorie());
        tvDisponibilitate.setText("Disponibil: " + (calculator.isDisponibilitate() ? "Da" : "Nu"));
        ratingBar.setRating(calculator.getRating());
        tvData.setText("Creat la: " + calculator.getDateTime());
        iconStatus.setText(calculator.getStatus() ? "✅ Activ" : "⛔ Inactiv");
        iconFunctional.setText(calculator.isFunctional() ? "⚙️ Funcțional" : "❌ Ne-funcțional");

        int baseColor = backgroundColors[position % backgroundColors.length];
        int darkerColor = darkenColor(baseColor, 0.85f);

        StateListDrawable selector = new StateListDrawable();
        selector.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(darkerColor));
        selector.addState(new int[]{}, new ColorDrawable(baseColor));
        cardRoot.setBackground(selector);

        // Aplică preferințe de culoare și dimensiune la text
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int dimensiune = prefs.getInt("dimensiune_text", 16);
        int culoare = prefs.getInt("culoare_text", Color.BLACK);

        tvNume.setTextSize(dimensiune);
        tvCategorie.setTextSize(dimensiune);
        tvDisponibilitate.setTextSize(dimensiune);
        tvData.setTextSize(dimensiune);
        iconStatus.setTextSize(dimensiune);
        iconFunctional.setTextSize(dimensiune);

        tvNume.setTextColor(culoare);
        tvCategorie.setTextColor(culoare);
        tvDisponibilitate.setTextColor(culoare);
        tvData.setTextColor(culoare);
        iconStatus.setTextColor(culoare);
        iconFunctional.setTextColor(culoare);

        return convertView;
    }
}