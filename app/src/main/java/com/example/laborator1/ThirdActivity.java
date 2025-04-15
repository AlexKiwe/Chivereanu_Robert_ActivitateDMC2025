package com.example.laborator1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String message = extras.getString("message");
            int num1 = extras.getInt("num1");
            int num2 = extras.getInt("num2");

            Toast.makeText(this, "Message: " + message + "\nNum1: " + num1 + "\nNum2: " + num2, Toast.LENGTH_LONG).show();

            int sum = num1 + num2;

            Button backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(v -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("responseMessage", "Sum calculated!");
                resultIntent.putExtra("sum", sum);
                setResult(RESULT_OK, resultIntent);
                finish();
            });
        }
    }
}
